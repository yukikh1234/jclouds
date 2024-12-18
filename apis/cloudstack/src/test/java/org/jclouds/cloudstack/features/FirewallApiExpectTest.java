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
package org.jclouds.cloudstack.features;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import org.jclouds.cloudstack.CloudStackContext;
import org.jclouds.cloudstack.domain.AsyncCreateResponse;
import org.jclouds.cloudstack.domain.FirewallRule;
import org.jclouds.cloudstack.domain.PortForwardingRule;
import org.jclouds.cloudstack.domain.Tag;
import org.jclouds.cloudstack.internal.BaseCloudStackExpectTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

/**
 * Test the CloudStack FirewallApi
 */
@Test(groups = "unit", testName = "FirewallApiExpectTest")
public class FirewallApiExpectTest extends BaseCloudStackExpectTest<FirewallApi> {

   public void testListFirewallRulesWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
            HttpRequest.builder()
                  .method("GET")
                  .endpoint("http://localhost:8080/client/api")
                  .addQueryParam("response", "json")
                  .addQueryParam("command", "listFirewallRules")
                  .addQueryParam("listAll", "true")
                  .addQueryParam("apiKey", "identity")
                  .addQueryParam("signature", "9+tdTXe2uYLzAexPNgrMy5Tq8hE=")
                  .addHeader("Accept", "application/json")
                  .build(),
            HttpResponse.builder()
                  .statusCode(200)
                  .payload(payloadFromResource("/listfirewallrulesresponse.json"))
                  .build());

      Set<String> CIDRs  = ImmutableSet.of("0.0.0.0/0");
      assertEquals(client.listFirewallRules(),
         ImmutableSet.of(
            FirewallRule.builder().id("2017").protocol(FirewallRule.Protocol.TCP).startPort(30)
               .endPort(35).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
               .CIDRs(CIDRs).build(),
            FirewallRule.builder().id("2016").protocol(FirewallRule.Protocol.TCP).startPort(22)
               .endPort(22).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
               .CIDRs(CIDRs).build(),
            FirewallRule.builder().id("10").protocol(FirewallRule.Protocol.TCP).startPort(22)
            .endPort(22).ipAddressId("8").ipAddress("10.27.27.57").state(FirewallRule.State.ACTIVE)
               .CIDRs(CIDRs).tags(Tag.builder().account("1").domain("ROOT").domainId("1").key("some-tag").resourceId("10")
                  .resourceType(Tag.ResourceType.FIREWALL_RULE).value("some-value").build()).build()
         ));
   }

   public void testListFirewallRulesWhenReponseIs404() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "listFirewallRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "9+tdTXe2uYLzAexPNgrMy5Tq8hE=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(404)
               .build());

      assertEquals(client.listFirewallRules(), ImmutableSet.of());
   }

   public void testGetFirewallRuleWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "listFirewallRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("id", "2017")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "6coh9Qdwla94TN1Dl008WlhzZUY=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/getfirewallrulesresponse.json"))
               .build());

      assertEquals(client.getFirewallRule("2017"),
         FirewallRule.builder().id("2017").protocol(FirewallRule.Protocol.TCP).startPort(30)
            .endPort(35).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
            .CIDRs(ImmutableSet.of("0.0.0.0/0")).build()
      );
   }

   public void testGetFirewallRuleWhenResponseIs404() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "listFirewallRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("id", "4")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "rYd8gr7ptdSZlIehBEMQEKsm07Q=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(404)
               .build());

      assertNull(client.getFirewallRule("4"));
   }

   public void testCreateFirewallRuleForIpAndProtocol() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "createFirewallRule")
               .addQueryParam("ipaddressid", "2")
               .addQueryParam("protocol", "TCP")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "d0MZ/yhQPAaV+YQmfZsQtQL2C28=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/createfirewallrulesresponse.json"))
               .build());

      AsyncCreateResponse response = client.createFirewallRuleForIpAndProtocol("2", FirewallRule.Protocol.TCP);
      assertEquals(response.getJobId(), "2036");
      assertEquals(response.getId(), "2017");
   }

   public void testDeleteFirewallRule() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "deleteFirewallRule")
               .addQueryParam("id", "2015")
               .addQueryParam("apiKey", "identity").
               addQueryParam("signature", "/T5FAO2yGPctaPmg7TEtIEFW3EU=")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/deletefirewallrulesresponse.json"))
               .build());

      client.deleteFirewallRule("2015");
   }

   public void testListPortForwardingRulesWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "listPortForwardingRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "8SXGJZWdcJfVz4V90Pyod12x9dM=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/listportforwardingrulesresponse.json"))
               .build());

      Set<String> cidrs = ImmutableSet.of("0.0.0.0/1", "128.0.0.0/1");

      assertEquals(client.listPortForwardingRules(),
            ImmutableSet.<PortForwardingRule>of(
                  PortForwardingRule.builder().id("18").privatePort(22).protocol(PortForwardingRule.Protocol.TCP)
                        .publicPort(22).virtualMachineId("89").virtualMachineName("i-3-89-VM").IPAddressId("34")
                        .IPAddress("72.52.126.63").state(PortForwardingRule.State.ACTIVE).build(),
                  PortForwardingRule.builder().id("15").privatePort(22).protocol(PortForwardingRule.Protocol.TCP)
                        .publicPort(2022).virtualMachineId("3").virtualMachineName("i-3-3-VM").IPAddressId("3")
                        .IPAddress("72.52.126.32").state(PortForwardingRule.State.ACTIVE)
                        .CIDRs(cidrs).tags(Tag.builder().account("1").domain("ROOT").domainId("1").key("some-tag").resourceId("15")
                        .resourceType(Tag.ResourceType.PORT_FORWARDING_RULE).value("some-value").build()).build()
                  )
      );
   }

   public void testListPortForwardingRulesWhenReponseIs404() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "listPortForwardingRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "8SXGJZWdcJfVz4V90Pyod12x9dM=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(404)
               .build());

      assertEquals(client.listPortForwardingRules(), ImmutableSet.of());
   }

   public void testGetPortForwardingRuleWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api").addQueryParam("response", "json")
               .addQueryParam("command", "listPortForwardingRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("id", "15")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "JL63p6cJzbb9vaffeV4u60IGlWE=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/getportforwardingrulesresponse.json"))
               .build());

      Set<String> cidrs = ImmutableSet.of("0.0.0.0/1", "128.0.0.0/1");

      assertEquals(client.getPortForwardingRule("15"),
            PortForwardingRule.builder().id("15").privatePort(22).protocol(PortForwardingRule.Protocol.TCP)
                  .publicPort(2022).virtualMachineId("3").virtualMachineName("i-3-3-VM").IPAddressId("3")
                  .IPAddress("72.52.126.32").state(PortForwardingRule.State.ACTIVE)
                  .CIDRs(cidrs).tags(Tag.builder().account("1").domain("ROOT").domainId("1").key("some-tag").resourceId("15")
                  .resourceType(Tag.ResourceType.PORT_FORWARDING_RULE).value("some-value").build()).build());
   }

   public void testGetPortForwardingRuleWhenResponseIs404() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api").addQueryParam("response", "json")
               .addQueryParam("command", "listPortForwardingRules")
               .addQueryParam("listAll", "true")
               .addQueryParam("id", "4")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "4blbBVn2+ZfF8HwoglbmtYoDAjs=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(404)
               .build());

      assertNull(client.getPortForwardingRule("4"));
   }

   public void testCreatePortForwardingRuleForVirtualMachine() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder().method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "createPortForwardingRule")
               .addQueryParam("ipaddressid", "2")
               .addQueryParam("protocol", "tcp")
               .addQueryParam("publicport", "22")
               .addQueryParam("virtualmachineid", "1234")
               .addQueryParam("privateport", "22")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "84dtGzQp0G6k3z3Gkc3F/HBNS2Y=")
               .addHeader("Accept", "application/json")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/createportforwardingrulesresponse.json"))
               .build());

      AsyncCreateResponse response = client.createPortForwardingRuleForVirtualMachine(
         "2", PortForwardingRule.Protocol.TCP, 22, "1234", 22);
      assertEquals(response.getJobId(), "2035");
      assertEquals(response.getId(), "2015");
   }

   public void testDeletePortForwardingRule() {
      FirewallApi client = requestSendsResponse(
         HttpRequest.builder()
               .method("GET")
               .endpoint("http://localhost:8080/client/api")
               .addQueryParam("response", "json")
               .addQueryParam("command", "deletePortForwardingRule")
               .addQueryParam("id", "2015")
               .addQueryParam("apiKey", "identity")
               .addQueryParam("signature", "2UE7KB3wm5ocmR+GMNFKPKfiDo8=")
               .build(),
         HttpResponse.builder()
               .statusCode(200)
               .payload(payloadFromResource("/deleteportforwardingrulesresponse.json"))
               .build());

      client.deletePortForwardingRule("2015");
   }

   public void testListEgressFirewallRulesWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "listEgressFirewallRules")
                    .addQueryParam("listAll", "true")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "j3OpRXs7mEwVKs9KIb4ncRKVO9A=")
                    .addHeader("Accept", "application/json")
                    .build(),
              HttpResponse.builder()
                    .statusCode(200)
                    .payload(payloadFromResource("/listegressfirewallrulesresponse.json"))
                    .build());

      Set<String> CIDRs  = ImmutableSet.of("0.0.0.0/0");
      assertEquals(client.listEgressFirewallRules(),
              ImmutableSet.of(
                    FirewallRule.builder().id("2017").protocol(FirewallRule.Protocol.TCP).startPort(30)
                          .endPort(35).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
                          .CIDRs(CIDRs).build(),
                    FirewallRule.builder().id("2016").protocol(FirewallRule.Protocol.TCP).startPort(22)
                          .endPort(22).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
                          .CIDRs(CIDRs).build(),
                    FirewallRule.builder().id("10").protocol(FirewallRule.Protocol.TCP).startPort(22)
                          .endPort(22).ipAddressId("8").ipAddress("10.27.27.57").state(FirewallRule.State.ACTIVE)
                          .CIDRs(CIDRs).tags(Tag.builder().account("1").domain("ROOT").domainId("1").key("some-tag").resourceId("10")
                          .resourceType(Tag.ResourceType.FIREWALL_RULE).value("some-value").build()).build()
              ));
   }

   public void testListEgressFirewallRulesWhenReponseIs404() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "listEgressFirewallRules")
                    .addQueryParam("listAll", "true")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "j3OpRXs7mEwVKs9KIb4ncRKVO9A=")
                    .addHeader("Accept", "application/json")
                    .build(),
              HttpResponse.builder()
                    .statusCode(404)
                    .build());

      assertEquals(client.listEgressFirewallRules(), ImmutableSet.of());
   }

   public void testGetEgressFirewallRuleWhenResponseIs2xx() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "listEgressFirewallRules")
                    .addQueryParam("listAll", "true")
                    .addQueryParam("id", "2017")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "Hi1K5VA3yd3mk0AmgJ2F6y+VzMo=")
                    .addHeader("Accept", "application/json")
                    .build(),
              HttpResponse.builder()
                    .statusCode(200)
                    .payload(payloadFromResource("/getegressfirewallrulesresponse.json"))
                    .build());

      assertEquals(client.getEgressFirewallRule("2017"),
              FirewallRule.builder().id("2017").protocol(FirewallRule.Protocol.TCP).startPort(30)
                      .endPort(35).ipAddressId("2").ipAddress("10.27.27.51").state(FirewallRule.State.ACTIVE)
                      .CIDRs(ImmutableSet.of("0.0.0.0/0")).build()
      );
   }

   public void testGetEgressFirewallRuleWhenResponseIs404() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "listEgressFirewallRules")
                    .addQueryParam("listAll", "true")
                    .addQueryParam("id", "4")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "dzb5azKxXZsuGrNRJbRHfna7FMo=")
                    .addHeader("Accept", "application/json")
                    .build(),
              HttpResponse.builder()
                    .statusCode(404)
                    .build());

      assertNull(client.getEgressFirewallRule("4"));
   }

   public void testCreateEgressFirewallRuleForNetworkAndProtocol() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "createEgressFirewallRule")
                    .addQueryParam("networkid", "2")
                    .addQueryParam("protocol", "TCP")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "I/OJEqiLp8ZHlZskKUiT5uTRE3M=")
                    .addHeader("Accept", "application/json")
                    .build(),
              HttpResponse.builder()
                    .statusCode(200)
                    .payload(payloadFromResource("/createegressfirewallrulesresponse.json"))
                    .build());

      AsyncCreateResponse response = client.createEgressFirewallRuleForNetworkAndProtocol("2", FirewallRule.Protocol.TCP);
      assertEquals(response.getJobId(), "2036");
      assertEquals(response.getId(), "2017");
   }

   public void testDeleteEgressFirewallRule() {
      FirewallApi client = requestSendsResponse(
              HttpRequest.builder()
                    .method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "deleteEgressFirewallRule")
                    .addQueryParam("id", "2015")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "S119WNmamKwc5d9qvvkIJznXytg=")
                    .build(),
              HttpResponse.builder()
                    .statusCode(200)
                    .payload(payloadFromResource("/deleteegressfirewallrulesresponse.json"))
                    .build());

      client.deleteEgressFirewallRule("2015");
   }
   @Override
   protected FirewallApi clientFrom(CloudStackContext context) {
      return context.getApi().getFirewallApi();
   }
}
