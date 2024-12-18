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
package org.jclouds.openstack.swift.v1.features;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.io.Payloads.newByteSourcePayload;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jclouds.openstack.swift.v1.domain.DeleteStaticLargeObjectResponse;
import org.jclouds.openstack.swift.v1.domain.Segment;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.internal.BaseSwiftApiLiveTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteSource;

@Test(groups = "live", testName = "StaticLargeObjectApiLiveTest", singleThreaded = true)
public class StaticLargeObjectApiLiveTest extends BaseSwiftApiLiveTest {

   private String defaultName = getClass().getSimpleName();
   private String defaultContainerName = getClass().getSimpleName() + "Container";
   private String unicodeName = getClass().getSimpleName() + "unic₪de";
   private String unicodeContainerName = getClass().getSimpleName() + "unic₪deContainer";
   private byte[] megOf1s;
   private byte[] megOf2s;

   public void testNotPresentWhenDeleting() throws Exception {
      for (String regionId : regions) {
         DeleteStaticLargeObjectResponse resp = getApi().getStaticLargeObjectApi(regionId, defaultContainerName).delete(UUID.randomUUID().toString());
         assertThat(resp.status()).isEqualTo("200 OK");
         assertThat(resp.deleted()).isZero();
         assertThat(resp.notFound()).isEqualTo(1);
         assertThat(resp.errors()).isEmpty();
      }
   }

   @Test
   public void testReplaceManifest() throws Exception {
      for (String regionId : regions) {
         assertReplaceManifest(regionId, defaultContainerName, defaultName);
      }
   }

   @Test(dependsOnMethods = "testReplaceManifest")
   public void testDelete() throws Exception {
      for (String regionId : regions) {
         assertDelete(regionId, defaultContainerName, defaultName);
      }
   }

   @Test
   public void testReplaceManifestUnicode() throws Exception {
      for (String regionId : regions) {
         assertReplaceManifest(regionId, unicodeContainerName, unicodeName);
      }
   }

   @Test(dependsOnMethods = "testReplaceManifestUnicode")
   public void testDeleteUnicode() throws Exception {
      for (String regionId : regions) {
         assertDelete(regionId, unicodeContainerName, unicodeName);
      }
   }

   public void testDeleteSinglePartObjectWithMultiPartDelete() throws Exception {
      String objectName = "testDeleteSinglePartObjectWithMultiPartDelete";
      for (String regionId : regions) {
         getApi().getObjectApi(regionId, defaultContainerName).put(objectName, newByteSourcePayload(ByteSource.wrap("swifty".getBytes())));
         DeleteStaticLargeObjectResponse resp = getApi().getStaticLargeObjectApi(regionId, defaultContainerName).delete(objectName);
         assertThat(resp.status()).isEqualTo("400 Bad Request");
         assertThat(resp.deleted()).isZero();
         assertThat(resp.notFound()).isZero();
         assertThat(resp.errors()).hasSize(1);
         getApi().getObjectApi(regionId, defaultContainerName).delete(objectName);
      }
   }

   protected void assertReplaceManifest(String regionId, String containerName, String name) {
      ObjectApi objectApi = getApi().getObjectApi(regionId, containerName);

      String etag1s = objectApi.put(name + "/1", newByteSourcePayload(ByteSource.wrap(megOf1s)));
      awaitConsistency();
      assertMegabyteAndETagMatches(regionId, containerName, name + "/1", etag1s);

      String etag2s = objectApi.put(name + "/2", newByteSourcePayload(ByteSource.wrap(megOf2s)));
      awaitConsistency();
      assertMegabyteAndETagMatches(regionId, containerName, name + "/2", etag2s);

      List<Segment> segments = ImmutableList.<Segment> builder()
          .add(Segment.builder()
              .path(format("%s/%s/1", containerName, name)).etag(etag1s).sizeBytes(1024 * 1024)
              .build())
          .add(Segment.builder()
              .path(format("%s/%s/2", containerName, name)).etag(etag2s).sizeBytes(1024 * 1024)
              .build())
          .build();

      awaitConsistency();
      String etagOfEtags = getApi().getStaticLargeObjectApi(regionId, containerName).replaceManifest(
          name, segments, ImmutableMap.of("myfoo", "Bar"));

      assertNotNull(etagOfEtags);

      awaitConsistency();

      SwiftObject bigObject = getApi().getObjectApi(regionId, containerName).get(name);
      assertEquals(bigObject.getETag(), etagOfEtags);
      assertEquals(bigObject.getPayload().getContentMetadata().getContentLength(), Long.valueOf(2 * 1024 * 1024));
      assertEquals(bigObject.getMetadata(), ImmutableMap.of("myfoo", "Bar"));

      // segments are visible
      assertEquals(getApi().getContainerApi(regionId).get(containerName).getObjectCount(), Long.valueOf(3));
   }

   protected void assertDelete(String regionId, String containerName, String name) {
      DeleteStaticLargeObjectResponse resp = getApi().getStaticLargeObjectApi(regionId, containerName).delete(name);
      assertThat(resp.status()).isEqualTo("200 OK");
      assertThat(resp.deleted()).isEqualTo(3);
      assertThat(resp.notFound()).isZero();
      assertThat(resp.errors()).isEmpty();
      assertEquals(getApi().getContainerApi(regionId).get(containerName).getObjectCount(), Long.valueOf(0));
   }

   protected void assertMegabyteAndETagMatches(String regionId, String containerName, String name, String etag1s) {
      SwiftObject object1s = getApi().getObjectApi(regionId, containerName).get(name);
      assertEquals(object1s.getETag(), etag1s);
      assertEquals(object1s.getPayload().getContentMetadata().getContentLength(), Long.valueOf(1024 * 1024));
   }

   @Override
   @BeforeClass(groups = "live")
   public void setup() {
      super.setup();
      for (String regionId : regions) {
         boolean created = getApi().getContainerApi(regionId).create(defaultContainerName);
         if (!created) {
            deleteAllObjectsInContainer(regionId, defaultContainerName);
         }

         created = getApi().getContainerApi(regionId).create(unicodeContainerName);
         if (!created) {
            deleteAllObjectsInContainer(regionId, unicodeContainerName);
         }
      }

      megOf1s = new byte[1024 * 1024];
      megOf2s = new byte[1024 * 1024];

      Arrays.fill(megOf1s, (byte) 1);
      Arrays.fill(megOf2s, (byte) 2);
   }

   @AfterClass(groups = "live")
   public void tearDown() {
      for (String regionId : regions) {
         deleteAllObjectsInContainer(regionId, defaultContainerName);
         getApi().getContainerApi(regionId).deleteIfEmpty(defaultContainerName);
         deleteAllObjectsInContainer(regionId, unicodeContainerName);
         getApi().getContainerApi(regionId).deleteIfEmpty(unicodeContainerName);
      }
   }
}
