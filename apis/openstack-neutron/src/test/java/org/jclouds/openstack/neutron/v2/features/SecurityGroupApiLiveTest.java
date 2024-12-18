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

package org.jclouds.openstack.neutron.v2.features;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jclouds.openstack.neutron.v2.domain.Rule;
import org.jclouds.openstack.neutron.v2.domain.RuleDirection;
import org.jclouds.openstack.neutron.v2.domain.RuleEthertype;
import org.jclouds.openstack.neutron.v2.domain.RuleProtocol;
import org.jclouds.openstack.neutron.v2.domain.SecurityGroup;
import org.jclouds.openstack.neutron.v2.internal.BaseNeutronApiLiveTest;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;

/**
 * Tests parsing and Guice wiring of RouterApi
 */
@Test(groups = "live", testName = "SecurityGroupApiLiveTest")
public class SecurityGroupApiLiveTest extends BaseNeutronApiLiveTest {

   /**
    * Smoke test for the Security Group extension for Neutron
    */
   public void testCreateUpdateAndDeleteSecurityGroup() {
      for (String region : api.getConfiguredRegions()) {
         SecurityGroupApi sgApi = null;
         Rule rule = null;
         SecurityGroup securityGroup = null;

         try {
            sgApi = api.getSecurityGroupApi(region);

            securityGroup = sgApi.create(
                  SecurityGroup.createBuilder().name("jclouds-test").description("jclouds test security group")
                        .build());
            assertNotNull(securityGroup);

            rule = sgApi.create(
                  Rule.createBuilder(RuleDirection.INGRESS, securityGroup.getId())
                        .ethertype(RuleEthertype.IPV6)
                        .portRangeMax(90)
                        .portRangeMin(80)
                        .protocol(RuleProtocol.TCP)
                        .build());

            assertNotNull(rule);

            // Refresh
            securityGroup = sgApi.getSecurityGroup(securityGroup.getId());

            assertEquals(securityGroup.getName(), "jclouds-test");
            assertEquals(securityGroup.getDescription(), "jclouds test security group");
            assertEquals(size(filter(securityGroup.getRules(), new Predicate<Rule>() {
               @Override
               public boolean apply(Rule input) {
                  return RuleDirection.INGRESS.equals(input.getDirection());
               }
            })), 1);

            Rule newSecGroupRule = null;

            for (Rule sgr : securityGroup.getRules()) {
               if (sgr.getId().equals(rule.getId())) {
                  newSecGroupRule = sgr;
                  break;
               }
            }
            assertNotNull(newSecGroupRule, "Did not find the new rule in the group.");

            assertEquals(rule, newSecGroupRule);

            assertEquals(rule.getEthertype(), RuleEthertype.IPV6);
            assertEquals(rule.getProtocol(), RuleProtocol.TCP);
            assertEquals(rule.getPortRangeMax().intValue(), 90);
            assertEquals(rule.getPortRangeMin().intValue(), 80);
            assertEquals(rule.getDirection(), RuleDirection.INGRESS);
         } finally {
            if (sgApi != null) {
               try {
                  if (rule != null) {
                     assertTrue(sgApi.deleteRule(rule.getId()));
                  }
               } finally {
                  assertTrue(sgApi.deleteSecurityGroup(securityGroup.getId()));
               }
            }
         }
      }
   }
}
