/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.aws.ec2.compute;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.compute.domain.OsFamily.AMZN_LINUX;
import static org.jclouds.compute.options.RunScriptOptions.Builder.runAsRoot;
import static org.jclouds.compute.util.ComputeServiceUtils.getCores;
import static org.jclouds.ec2.util.IpPermissions.permit;
import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.jclouds.ContextBuilder;
import org.jclouds.aws.cloudwatch.AWSCloudWatchProviderMetadata;
import org.jclouds.aws.ec2.AWSEC2Api;
import org.jclouds.aws.ec2.domain.AWSRunningInstance;
import org.jclouds.aws.ec2.domain.MonitoringState;
import org.jclouds.aws.ec2.features.AWSSecurityGroupApi;
import org.jclouds.cloudwatch.CloudWatchApi;
import org.jclouds.cloudwatch.domain.Dimension;
import org.jclouds.cloudwatch.domain.EC2Constants;
import org.jclouds.cloudwatch.domain.GetMetricStatistics;
import org.jclouds.cloudwatch.domain.GetMetricStatisticsResponse;
import org.jclouds.cloudwatch.domain.Statistics;
import org.jclouds.cloudwatch.domain.Unit;
import org.jclouds.compute.domain.ExecResponse;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.predicates.NodePredicates;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.ec2.compute.EC2ComputeServiceLiveTest;
import org.jclouds.ec2.domain.KeyPair;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.InstanceApi;
import org.jclouds.ec2.features.KeyPairApi;
import org.jclouds.net.domain.IpProtocol;
import org.jclouds.scriptbuilder.domain.Statements;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.ListenableFuture;

@Test(groups = "live", singleThreaded = true, testName = "AWSEC2ComputeServiceLiveTest")
public class AWSEC2ComputeServiceLiveTest extends EC2ComputeServiceLiveTest {

   public AWSEC2ComputeServiceLiveTest() {
      provider = "aws-ec2";
      group = "ec2";
   }

   @Override
   protected void checkVolumes(Hardware hardware) {
      // Not all hardware profiles define volumes. Don't check their size
   }

   @Override
   @Test
   public void testExtendedOptionsAndLogin() throws Exception {
      String region = "us-west-2";

      AWSSecurityGroupApi securityGroupApi = view.unwrapApi(AWSEC2Api.class).getSecurityGroupApi().get();

      KeyPairApi keyPairApi = view.unwrapApi(AWSEC2Api.class).getKeyPairApi().get();

      InstanceApi instanceApi = view.unwrapApi(AWSEC2Api.class).getInstanceApi().get();

      String group = this.group + "o";

      Date before = new Date();

      ImmutableMap<String, String> userMetadata = ImmutableMap.<String, String> of("test", group);

      ImmutableSet<String> tags = ImmutableSet.of(group);

      // note that if you change the location, you must also specify image parameters
      Template template = client.templateBuilder().locationId(region).osFamily(AMZN_LINUX).os64Bit(true).build();
      template.getOptions().tags(tags);
      template.getOptions().userMetadata(userMetadata);
      template.getOptions().tags(tags);
      template.getOptions().as(AWSEC2TemplateOptions.class).enableMonitoring();
      template.getOptions().as(AWSEC2TemplateOptions.class).spotPrice(0.05f);

      String startedId = null;
      try {
         cleanupExtendedStuffInRegion(region, securityGroupApi, keyPairApi, group);

         Thread.sleep(3000);  // eventual consistency if deletes actually occurred.

         // create a security group that allows ssh in so that our scripts later
         // will work
         String groupId = securityGroupApi.createSecurityGroupInRegionAndReturnId(region, group, group);

         securityGroupApi.authorizeSecurityGroupIngressInRegion(region, groupId, permit(IpProtocol.TCP).port(22));

         template.getOptions().as(AWSEC2TemplateOptions.class).securityGroupIds(groupId);

         // create a keypair to pass in as well
         KeyPair result = keyPairApi.createKeyPairInRegion(region, group);
         template.getOptions().as(AWSEC2TemplateOptions.class).keyPair(result.getKeyName());

         // pass in the private key, so that we can run a script with it
         assert result.getKeyMaterial() != null : result;
         template.getOptions().overrideLoginPrivateKey(result.getKeyMaterial());

         Set<? extends NodeMetadata> nodes = client.createNodesInGroup(group, 1, template);
         NodeMetadata first = getOnlyElement(nodes);

         checkUserMetadataContains(first, userMetadata);
         checkTagsInNodeEquals(first, tags);

         assert first.getCredentials() != null : first;
         assert first.getCredentials().identity != null : first;

         startedId = first.getProviderId();

         Reservation<? extends RunningInstance> reservation = getOnlyElement(instanceApi
                  .describeInstancesInRegion(region, startedId));
         AWSRunningInstance instance = AWSRunningInstance.class.cast(getOnlyElement(reservation));

         assertEquals(instance.getKeyName(), group);
         assert instance.getSpotInstanceRequestId() != null;
         assertEquals(instance.getMonitoringState(), MonitoringState.ENABLED);

         // generate some load
         ListenableFuture<ExecResponse> future = client.submitScriptOnNode(first.getId(), Statements
                  .exec("while true; do true; done"), runAsRoot(false).nameTask("cpuSpinner"));

         // monitoring granularity for free tier is 5 minutes, so lets make sure we have data.
         Thread.sleep(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES));

         // stop the spinner
         future.cancel(true);

         CloudWatchApi monitoringApi = ContextBuilder.newBuilder(new AWSCloudWatchProviderMetadata())
                                                     .credentials(identity, credential)
                                                     .modules(setupModules())
                                                     .buildApi(CloudWatchApi.class);

         try {
            GetMetricStatisticsResponse datapoints = monitoringApi.getMetricApiForRegion(instance.getRegion())
                     .getMetricStatistics(GetMetricStatistics.builder()
                                                             .dimension(new Dimension(EC2Constants.Dimension.INSTANCE_ID, instance.getId()))
                                                             .unit(Unit.PERCENT)
                                                             .namespace("AWS/EC2")
                                                             .metricName("CPUUtilization")
                                                             .startTime(before)
                                                             .endTime(new Date())
                                                             .period(60)
                                                             .statistic(Statistics.AVERAGE)
                                                             .build());
            assert !datapoints.isEmpty() : instance;
         } finally {
            monitoringApi.close();
         }

         // try to run a script with the original keyPair
         runScriptWithCreds(group, first.getOperatingSystem(), LoginCredentials.builder().user(
                  first.getCredentials().identity).privateKey(result.getKeyMaterial()).build());

      } finally {
         client.destroyNodesMatching(NodePredicates.inGroup(group));
         if (startedId != null) {
            // ensure we didn't delete these resources!
            assertEquals(keyPairApi.describeKeyPairsInRegion(region, group).size(), 1);
            assertEquals(securityGroupApi.describeSecurityGroupsInRegion(region, group).size(), 1);
         }
         cleanupExtendedStuffInRegion(region, securityGroupApi, keyPairApi, group);
      }
   }
   
   @Override
   protected void doCompareSizes() throws Exception {
      Hardware defaultSize = view.getComputeService().templateBuilder().build().getHardware();

      Hardware smallest = view.getComputeService().templateBuilder().smallest().build().getHardware();
      Hardware fastest = view.getComputeService().templateBuilder().fastest().build().getHardware();
      Hardware biggest = view.getComputeService().templateBuilder().biggest().build().getHardware();

      assertEquals(defaultSize, smallest);

      assert getCores(smallest) <= getCores(fastest) : String.format("%s ! <= %s", smallest, fastest);
      // m4.10xlarge is slower but has more cores than c4.8xlarge
      // assert getCores(biggest) <= getCores(fastest) : String.format("%s ! <= %s", biggest, fastest);
      // assert getCores(fastest) >= getCores(biggest) : String.format("%s ! >= %s", fastest, biggest);

      assert biggest.getRam() >= fastest.getRam() : String.format("%s ! >= %s", biggest, fastest);
      assert biggest.getRam() >= smallest.getRam() : String.format("%s ! >= %s", biggest, smallest);

      assert getCores(fastest) >= getCores(smallest) : String.format("%s ! >= %s", fastest, smallest);
   }
}
