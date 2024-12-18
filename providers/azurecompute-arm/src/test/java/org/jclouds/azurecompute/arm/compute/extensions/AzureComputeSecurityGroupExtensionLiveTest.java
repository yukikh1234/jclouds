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
package org.jclouds.azurecompute.arm.compute.extensions;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.jclouds.azurecompute.arm.compute.options.AzureTemplateOptions.Builder.resourceGroup;
import static org.jclouds.azurecompute.arm.config.AzureComputeProperties.TIMEOUT_RESOURCE_DELETED;
import static org.jclouds.compute.predicates.NodePredicates.inGroup;
import static org.jclouds.net.domain.IpProtocol.TCP;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URI;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.jclouds.azurecompute.arm.AzureComputeApi;
import org.jclouds.azurecompute.arm.AzureComputeProviderMetadata;
import org.jclouds.azurecompute.arm.compute.options.AzureTemplateOptions;
import org.jclouds.azurecompute.arm.internal.AzureLiveTestUtils;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.SecurityGroup;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.extensions.SecurityGroupExtension;
import org.jclouds.compute.extensions.internal.BaseSecurityGroupExtensionLiveTest;
import org.jclouds.domain.Location;
import org.jclouds.net.domain.IpPermission;
import org.jclouds.net.util.IpPermissions;
import org.jclouds.providers.ProviderMetadata;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

/**
 * Live test for AzureCompute
 * {@link org.jclouds.compute.extensions.SecurityGroupExtension} implementation.
 */
@Test(groups = "live", singleThreaded = true, testName = "AzureComputeSecurityGroupExtensionLiveTest")
public class AzureComputeSecurityGroupExtensionLiveTest extends BaseSecurityGroupExtensionLiveTest {

   private Predicate<URI> resourceDeleted;
   private String resourceGroupName;

   public AzureComputeSecurityGroupExtensionLiveTest() {
      provider = "azurecompute-arm";
      resourceGroupName = "sgelivetest";
   }

   @BeforeClass(groups = { "integration", "live" })
   public void setupContext() {
      super.setupContext();
      resourceDeleted = context.utils().injector().getInstance(Key.get(new TypeLiteral<Predicate<URI>>() {
      }, Names.named(TIMEOUT_RESOURCE_DELETED)));
      createResourceGroup(resourceGroupName);
   }

   @Test(groups = { "integration", "live" }, singleThreaded = true, dependsOnMethods = "testAddIpPermissionsFromSpec")
   public void testAddIpPermissionForAnyProtocol() {
      ComputeService computeService = view.getComputeService();
      Optional<SecurityGroupExtension> securityGroupExtension = computeService.getSecurityGroupExtension();
      assertTrue(securityGroupExtension.isPresent(), "security group extension was not present");

      SecurityGroup group = securityGroupExtension.get().getSecurityGroupById(groupId);
      assertNotNull(group, "No security group was found with id: " + groupId);

      IpPermission openAll = IpPermissions.permitAnyProtocol();
      SecurityGroup allOpenSecurityGroup = securityGroupExtension.get().addIpPermission(openAll, group);

      assertTrue(allOpenSecurityGroup.getIpPermissions().contains(openAll));
   }

   @Test(groups = { "integration", "live" }, dependsOnMethods = "testCreateSecurityGroup")
   public void testCreateNodeWithSecurityGroup() throws RunNodesException, InterruptedException, ExecutionException {
      ComputeService computeService = view.getComputeService();
      Optional<SecurityGroupExtension> securityGroupExtension = computeService.getSecurityGroupExtension();
      assertTrue(securityGroupExtension.isPresent(), "security group extension was not present");

      NodeMetadata node = getOnlyElement(computeService.createNodesInGroup(nodeGroup, 1,
            options().securityGroups(groupId)));

      try {
         Set<SecurityGroup> groups = securityGroupExtension.get().listSecurityGroupsForNode(node.getId());
         assertEquals(groups.size(), 1, "node has " + groups.size() + " groups");
         assertEquals(getOnlyElement(groups).getId(), groupId);
      } finally {
         computeService.destroyNodesMatching(inGroup(node.getGroup()));
      }
   }

   @Test(groups = { "integration", "live" }, dependsOnMethods = "testCreateSecurityGroup")
   public void testCreateNodeWithInboundPorts() throws RunNodesException, InterruptedException, ExecutionException {
      ComputeService computeService = view.getComputeService();
      Optional<SecurityGroupExtension> securityGroupExtension = computeService.getSecurityGroupExtension();
      assertTrue(securityGroupExtension.isPresent(), "security group extension was not present");

      NodeMetadata node = getOnlyElement(computeService.createNodesInGroup(nodeGroup, 1,
            options().inboundPorts(22, 23, 24, 8000)));

      try {
         Set<SecurityGroup> groups = securityGroupExtension.get().listSecurityGroupsForNode(node.getId());
         assertEquals(groups.size(), 1, "node has " + groups.size() + " groups");

         SecurityGroup group = getOnlyElement(groups);
         assertEquals(group.getIpPermissions().size(), 2);
         assertTrue(group.getIpPermissions().contains(IpPermissions.permit(TCP).fromPort(22).to(24)));
         assertTrue(group.getIpPermissions().contains(IpPermissions.permit(TCP).port(8000)));
      } finally {
         computeService.destroyNodesMatching(inGroup(node.getGroup()));
      }
   }

   @AfterClass(groups = { "integration", "live" })
   @Override
   protected void tearDownContext() {
      try {
         URI uri = view.unwrapApi(AzureComputeApi.class).getResourceGroupApi().delete(resourceGroupName);
         if (uri != null) {
            assertTrue(resourceDeleted.apply(uri),
                  String.format("Resource %s was not terminated in the configured timeout", uri));
         }
      } finally {
         super.tearDownContext();
      }
   }

   @Override
   protected Properties setupProperties() {
      Properties properties = super.setupProperties();
      AzureLiveTestUtils.defaultProperties(properties);
      setIfTestSystemPropertyPresent(properties, "oauth.endpoint");
      return properties;
   }

   @Override
   protected ProviderMetadata createProviderMetadata() {
      return AzureComputeProviderMetadata.builder().build();
   }
   
   private AzureTemplateOptions options() {
      return resourceGroup(resourceGroupName);
   }
   
   @Override
   public Template getNodeTemplate() {
      return view.getComputeService().templateBuilder().options(options()).build();
   }

   private void createResourceGroup(String name) {
      Location location = getNodeTemplate().getLocation();
      view.unwrapApi(AzureComputeApi.class).getResourceGroupApi().create(name, location.getId(), null);
   }
}
