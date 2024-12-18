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

import org.jclouds.cloudstack.CloudStackContext;
import org.jclouds.cloudstack.domain.NetworkType;
import org.jclouds.cloudstack.domain.Tag;
import org.jclouds.cloudstack.domain.Zone;
import org.jclouds.cloudstack.internal.BaseCloudStackExpectTest;
import org.jclouds.http.HttpRequest;
import org.jclouds.http.HttpResponse;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;

/**
 * Test the CloudStack ZoneApi
 */
@Test(groups = "unit", testName = "ZoneApiExpectTest")
public class ZoneApiExpectTest extends BaseCloudStackExpectTest<ZoneApi> {


   public void testListZonesWhenResponseIs2xx() {
      ZoneApi client = requestSendsResponse(
         HttpRequest.builder().method("GET")
                    .endpoint("http://localhost:8080/client/api")
                    .addQueryParam("response", "json")
                    .addQueryParam("command", "listZones")
                    .addQueryParam("listAll", "true")
                    .addQueryParam("apiKey", "identity")
                    .addQueryParam("signature", "8iHCtck0qfxFTqJ8reyAObRf31I=")
                    .addHeader("Accept", "application/json")
                    .build(),
         HttpResponse.builder()
                     .statusCode(200)
                     .payload(payloadFromResource("/listzonesresponse.json"))
                     .build());

      assertEquals(client.listZones(),
         ImmutableSet.of(
            Zone.builder()
               .id("1")
               .name("San Jose 1")
               .networkType(NetworkType.ADVANCED)
               .securityGroupsEnabled(false).build(),
            Zone.builder()
                  .id("2")
                  .name("Chicago")
                  .networkType(NetworkType.ADVANCED)
                  .securityGroupsEnabled(true)
                  .tags(Tag.builder()
                        .account("1")
                        .domain("ROOT")
                        .domainId("1")
                        .key("some-tag")
                        .resourceId("2")
                        .resourceType(Tag.ResourceType.ZONE)
                        .value("some-value")
                        .build())
                  .build()));
   }

   public void testListZonesWhenResponseIs404() {
      ZoneApi client = requestSendsResponse(
            HttpRequest.builder()
                  .method("GET")
                  .endpoint("http://localhost:8080/client/api")
                  .addQueryParam("response", "json")
                  .addQueryParam("command", "listZones")
                  .addQueryParam("listAll", "true")
                  .addQueryParam("apiKey", "identity")
                  .addQueryParam("signature", "8iHCtck0qfxFTqJ8reyAObRf31I=")
                  .addHeader("Accept", "application/json")
            .build(),
         HttpResponse.builder()
            .statusCode(404)
            .build());

      assertEquals(client.listZones(), ImmutableSet.of());
   }

   @Override
   protected ZoneApi clientFrom(CloudStackContext context) {
      return context.getApi().getZoneApi();
   }
}
