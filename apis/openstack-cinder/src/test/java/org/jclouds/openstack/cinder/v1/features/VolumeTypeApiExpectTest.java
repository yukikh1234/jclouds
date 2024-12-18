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
package org.jclouds.openstack.cinder.v1.features;

import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.Set;

import org.jclouds.date.DateService;
import org.jclouds.date.internal.SimpleDateFormatDateService;
import org.jclouds.http.HttpResponse;
import org.jclouds.openstack.cinder.v1.domain.VolumeType;
import org.jclouds.openstack.cinder.v1.internal.BaseCinderApiExpectTest;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests Guice wiring and parsing of VolumeTypeApi
 */
@Test(groups = "unit", testName = "VolumeTypeApiExpectTest")
public class VolumeTypeApiExpectTest extends BaseCinderApiExpectTest {
   private DateService dateService = new SimpleDateFormatDateService();

   public void testListVolumeTypes() {
      URI endpoint = URI.create("http://172.16.0.1:8776/v1/50cdb4c60374463198695d9f798fa34d/types");
      VolumeTypeApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(200).payload(payloadFromResource("/volume_type_list_simple.json")).build()
      ).getVolumeTypeApi("RegionOne");

      Set<? extends VolumeType> types = api.list().toSet();
      assertEquals(types, testVolumeTypes());
   }

   public void testGetVolumeType() {
      URI endpoint = URI.create("http://172.16.0.1:8776/v1/50cdb4c60374463198695d9f798fa34d/types/1");
      VolumeTypeApi api = requestsSendResponses(
            keystoneAuthWithUsernameAndPasswordAndTenantName,
            responseWithKeystoneAccess,
            authenticatedGET().endpoint(endpoint).build(),
            HttpResponse.builder().statusCode(200).payload(payloadFromResource("/volume_type_get.json")).build()
      ).getVolumeTypeApi("RegionOne");

      VolumeType type = api.get("1");
      assertEquals(type, testVolumeType());
   }

   public VolumeType testVolumeType() {
      return VolumeType.builder()
            .id("1")
            .name("jclouds-test-1")
            .created(dateService.iso8601SecondsDateParse("2012-05-10 12:33:06"))
            .extraSpecs(ImmutableMap.of("test", "value1", "test1", "wibble"))
            .build();
   }
   public Set<VolumeType> testVolumeTypes() {
      VolumeType firstVolumeType = testVolumeType();
      VolumeType secondVolumeTypeWithEmptyExtraSpecs = VolumeType.builder()
              .id("2")
              .name("jclouds-test-2")
              .created(dateService.iso8601SecondsDateParse("2012-05-10 12:33:06"))
              .build();
      VolumeType thirdVolumeTypeWithNullableExtraSpecs = VolumeType.builder()
              .id("3")
              .name("jclouds-test-3")
              .created(dateService.iso8601SecondsDateParse("2012-05-10 12:33:06"))
              .extraSpecs(null)
              .build();
      return ImmutableSet.of(firstVolumeType, secondVolumeTypeWithEmptyExtraSpecs, thirdVolumeTypeWithNullableExtraSpecs);
   }
}
