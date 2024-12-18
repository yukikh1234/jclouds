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

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.http.options.GetOptions.Builder.tail;
import static org.jclouds.io.Payloads.newByteSourcePayload;
import static org.jclouds.openstack.swift.v1.options.ListContainerOptions.Builder.marker;
import static org.jclouds.util.Strings2.toStringAndClose;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Fail;
import org.jclouds.blobstore.KeyNotFoundException;
import org.jclouds.http.HttpResponseException;
import org.jclouds.http.options.GetOptions;
import org.jclouds.io.Payload;
import org.jclouds.openstack.swift.v1.domain.ObjectList;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.internal.BaseSwiftApiLiveTest;
import org.jclouds.openstack.swift.v1.options.CopyOptions;
import org.jclouds.openstack.swift.v1.options.ListContainerOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.io.ByteSource;

/**
 * Provides live tests for the {@link ObjectApi}.
 */
@Test(groups = "live", testName = "ObjectApiLiveTest", singleThreaded = true)
public class ObjectApiLiveTest extends BaseSwiftApiLiveTest {

   private String name = getClass().getSimpleName();
   private String containerName = getClass().getSimpleName() + "Container";
   static final Payload PAYLOAD = newByteSourcePayload(ByteSource.wrap("swifty".getBytes()));

   protected void assertCanCreateReadUpdateDeleteList(String regionId, String containerName, String objectName) throws Exception {
      assertNotNull(getApi().getContainerApi(regionId).create(containerName));
      assertNotNull(getApi().getObjectApi(regionId, containerName).put(objectName, PAYLOAD));

      SwiftObject object = getApi().getObjectApi(regionId, containerName).get(objectName);
      assertEquals(object.getName(), objectName);
      checkObject(object);
      assertEquals(toStringAndClose(object.getPayload().openStream()), "swifty");

      String lexicographicallyBeforeName = objectName.substring(0, objectName.length() - 1);
      object = getApi().getObjectApi(regionId, containerName)
          .list(marker(lexicographicallyBeforeName)).get(0);
      assertEquals(object.getName(), objectName);
      checkObject(object);

      getApi().getObjectApi(regionId, containerName).delete(objectName);
      getApi().getContainerApi(regionId).deleteIfEmpty(containerName);
   }

   public void testCreateWithSpacesAndSpecialCharacters() throws Exception {
      final String containerName = "container # ! special";
      final String objectName = "object # ! special";

      for (String regionId : regions) {
         assertCanCreateReadUpdateDeleteList(regionId, containerName, objectName);
      }
   }

   public void testCreateAndListWithUnicodeCharacters() throws Exception {
      final String containerName = "container-unic₪de";
      final String objectName = "object-unic₪de";

      for (String regionId : regions) {
         assertCanCreateReadUpdateDeleteList(regionId, containerName, objectName);
      }
   }

   public void testPutWithExpiration() throws Exception {
      String objectName = "test-expiration";

      long expireMillis = new Date().getTime() + 1000 * 60 * 60 * 24;
      Date expireAt = new Date(expireMillis);

      Payload payload = newByteSourcePayload(ByteSource.wrap("swifty".getBytes()));
      payload.getContentMetadata().setExpires(expireAt);

      for (String regionId : regions) {
         String etag = getApi().getObjectApi(regionId, containerName).put(objectName, payload);
         assertNotNull(etag);

         SwiftObject object = getApi().getObjectApi(regionId, containerName).get(objectName);
         assertEquals(object.getName(), objectName);
         checkObject(object);
         assertEquals(toStringAndClose(object.getPayload().openStream()), "swifty");

         getApi().getObjectApi(regionId, containerName).delete(objectName);
      }
   }

   public void testCopyObject() throws Exception {
      for (String regionId : regions) {
         // source
         String sourceContainer = "src" + containerName;
         String sourceObjectName = "original.txt";
         String badSource = "badSource";

         // destination
         String destinationContainer = "dest" + containerName;
         String destinationObject = "copy.txt";
         String destinationPath = "/" + destinationContainer + "/" + destinationObject;

         ContainerApi containerApi = getApi().getContainerApi(regionId);

         // create source and destination dirs
         containerApi.create(sourceContainer);
         containerApi.create(destinationContainer);

         // get the api for this region and container
         ObjectApi srcApi = getApi().getObjectApi(regionId, sourceContainer);
         ObjectApi destApi = getApi().getObjectApi(regionId, destinationContainer);

         // Create source object
         assertNotNull(srcApi.put(sourceObjectName, PAYLOAD));
         SwiftObject sourceObject = srcApi.get(sourceObjectName);
         checkObject(sourceObject);

         // Create the destination object
         assertNotNull(destApi.put(destinationObject, PAYLOAD));
         SwiftObject object = destApi.get(destinationObject);
         checkObject(object);

         // check the copy operation
         destApi.copy(destinationObject, sourceContainer, sourceObjectName);
         assertNotNull(destApi.get(destinationObject));

         // now get a real SwiftObject
         SwiftObject destSwiftObject = destApi.get(destinationObject);
         assertEquals(toStringAndClose(destSwiftObject.getPayload().openStream()), "swifty");

         // test exception thrown on bad source name
         try {
            destApi.copy(destinationObject, badSource, sourceObjectName);
         } catch (KeyNotFoundException e) {
            continue;
         } finally {
            deleteAllObjectsInContainer(regionId, sourceContainer);
            containerApi.deleteIfEmpty(sourceContainer);

            deleteAllObjectsInContainer(regionId, destinationContainer);
            containerApi.deleteIfEmpty(destinationContainer);
         }
         fail("Expected KeyNotFoundException");
      }
   }

   public void testCopyObjectWithMetadata() throws Exception {
      for (String regionId : regions) {
         // source
         String sourceContainer = "src" + containerName;
         String sourceObjectName = "original.txt";
         String badSource = "badSource";

         // destination
         String destinationContainer = "dest" + containerName;
         String destinationObject = "copy.txt";
         String destinationPath = "/" + destinationContainer + "/" + destinationObject;

         ContainerApi containerApi = getApi().getContainerApi(regionId);

         // create source and destination dirs
         containerApi.create(sourceContainer);
         containerApi.create(destinationContainer);

         // get the api for this region and container
         ObjectApi srcApi = getApi().getObjectApi(regionId, sourceContainer);
         ObjectApi destApi = getApi().getObjectApi(regionId, destinationContainer);

         // Create source object
         assertNotNull(srcApi.put(sourceObjectName, PAYLOAD));
         SwiftObject sourceObject = srcApi.get(sourceObjectName);
         checkObject(sourceObject);

         srcApi.updateMetadata(sourceObjectName,
               ImmutableMap.of("userProvidedMetadataKey", "userProvidedMetadataValue"));

         // Create the destination object
         assertNotNull(destApi.put(destinationObject, PAYLOAD));
         SwiftObject object = destApi.get(destinationObject);
         checkObject(object);

         // check the copy append metadata operation
         destApi.copyAppendMetadata(destinationObject, sourceContainer, sourceObjectName,
               ImmutableMap.<String, String>of("additionalUserMetakey", "additionalUserMetavalue"),
               ImmutableMap.of("Content-Disposition", "attachment; filename=\"updatedname.txt\""));

         // now get a real SwiftObject
         SwiftObject destSwiftObject = destApi.get(destinationObject);
         assertEquals(toStringAndClose(destSwiftObject.getPayload().openStream()), "swifty");

         /**
          * Make sure all src metadata is in dest
          * Make sure the new content disposition is in dest
          */
         Multimap<String, String> srcHeaders = null;
         Multimap<String, String> destHeaders = null;
         srcHeaders = srcApi.getWithoutBody(sourceObjectName).getHeaders();
         destHeaders = destSwiftObject.getHeaders();
         for (Entry<String, String> header : srcHeaders.entries()) {
            if (header.getKey().equals("Date"))continue;
            if (header.getKey().equals("Last-Modified"))continue;
            if (header.getKey().equals("X-Trans-Id"))continue;
            if (header.getKey().equals("X-Timestamp"))continue;
            if (header.getKey().equals("X-Openstack-Request-Id"))continue;
            assertTrue(destHeaders.containsEntry(header.getKey(), header.getValue()), "Could not find: " + header);
         }
         assertEquals(destSwiftObject.getPayload().getContentMetadata().getContentDisposition(), "attachment; filename=\"updatedname.txt\"");

         // check the copy replace metadata operation
         destApi.copy(destinationObject, sourceContainer, sourceObjectName,
               ImmutableMap.<String, String>of("key3", "value3"),
               ImmutableMap.of("Content-Disposition", "attachment; filename=\"updatedname.txt\""));

         // now get a real SwiftObject
         destSwiftObject = destApi.get(destinationObject);
         assertEquals(toStringAndClose(destSwiftObject.getPayload().openStream()), "swifty");

         destHeaders = destSwiftObject.getHeaders();
         assertThat(destHeaders.get("X-Object-Meta-Key3")).containsExactly("value3");
         assertEquals(destSwiftObject.getPayload().getContentMetadata().getContentDisposition(), "attachment; filename=\"updatedname.txt\"");

         // test exception thrown on bad source name
         try {
            destApi.copy(destinationObject, badSource, sourceObjectName);
         } catch (KeyNotFoundException e) {
            continue;
         } finally {
            deleteAllObjectsInContainer(regionId, sourceContainer);
            containerApi.deleteIfEmpty(sourceContainer);

            deleteAllObjectsInContainer(regionId, destinationContainer);
            containerApi.deleteIfEmpty(destinationContainer);
         }
         fail("Expected KeyNotFoundException");
      }
   }

   public void testCopyObjectConditional() throws Exception {
      for (String regionId : regions) {
         // source
         String sourceContainer = "src" + containerName;
         String sourceObjectName = "original.txt";
         String badSource = "badSource";

         // destination
         String destinationContainer = "dest" + containerName;
         String destinationObject = "copy.txt";
         String destinationPath = "/" + destinationContainer + "/" + destinationObject;

         ContainerApi containerApi = getApi().getContainerApi(regionId);

         // create source and destination dirs
         containerApi.create(sourceContainer);
         containerApi.create(destinationContainer);

         // get the api for this region and container
         ObjectApi srcApi = getApi().getObjectApi(regionId, sourceContainer);
         ObjectApi destApi = getApi().getObjectApi(regionId, destinationContainer);

         // Create source object
         assertNotNull(srcApi.put(sourceObjectName, PAYLOAD));
         SwiftObject sourceObject = srcApi.get(sourceObjectName);
         checkObject(sourceObject);

         destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifMatch(sourceObject.getETag()));
         try {
            destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifMatch("fake-etag"));
            Fail.failBecauseExceptionWasNotThrown(HttpResponseException.class);
         } catch (HttpResponseException hre) {
            assertThat(hre.getResponse().getStatusCode()).isEqualTo(412);
         }

         long now = System.currentTimeMillis();
         Date before = new Date(now - 1000 * 1000);
         Date after = new Date(now + 1000 * 1000);

         destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifModifiedSince(before));
         try {
            destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifModifiedSince(after));
            Fail.failBecauseExceptionWasNotThrown(HttpResponseException.class);
         } catch (HttpResponseException hre) {
            assertThat(hre.getResponse().getStatusCode()).isEqualTo(304);
         }

         try {
            destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifUnmodifiedSince(before));
            Fail.failBecauseExceptionWasNotThrown(HttpResponseException.class);
         } catch (HttpResponseException hre) {
            assertThat(hre.getResponse().getStatusCode()).isEqualTo(412);
         }
         destApi.copy(destinationObject, sourceContainer, sourceObjectName, new CopyOptions().ifUnmodifiedSince(after));
      }
   }

   public void testList() throws Exception {
      for (String regionId : regions) {
         ObjectApi objectApi = getApi().getObjectApi(regionId, containerName);
         ObjectList response = objectApi.list();
         assertEquals(response.getContainer(), getApi().getContainerApi(regionId).get(containerName));
         for (SwiftObject object : response) {
            checkObject(object);
         }
      }
   }

   public void testListWithOptions() throws Exception {
      for (String regionId : regions) {
         ObjectApi objectApi = getApi().getObjectApi(regionId, containerName);
         ObjectList response = objectApi.list(ListContainerOptions.NONE);
         assertEquals(response.getContainer(), getApi().getContainerApi(regionId).get(containerName));
         for (SwiftObject object : response) {
            checkObject(object);
         }
      }
   }

   public void testMetadata() throws Exception {
      for (String regionId : regions) {
         SwiftObject object = getApi().getObjectApi(regionId, containerName).get(name);
         assertEquals(object.getName(), name);
         checkObject(object);
         assertEquals(toStringAndClose(object.getPayload().openStream()), "swifty");
      }
   }

   public void testUpdateMetadata() throws Exception {
      for (String regionId : regions) {
         ObjectApi objectApi = getApi().getObjectApi(regionId, containerName);

         Map<String, String> meta = ImmutableMap.of("MyAdd1", "foo", "MyAdd2", "bar");
         objectApi.updateMetadata(name, meta);

         SwiftObject object = objectApi.get(name);
         for (Entry<String, String> entry : meta.entrySet()) {
            // note keys are returned in lower-case!
            assertEquals(object.getMetadata().get(entry.getKey().toLowerCase()), entry.getValue(),
                  object + " didn't have metadata: " + entry);
         }
      }
   }

   public void testGet() throws Exception {
      for (String regionId : regions) {
         SwiftObject object = getApi().getObjectApi(regionId, containerName).get(name, GetOptions.NONE);
         assertEquals(object.getName(), name);
         checkObject(object);
         assertEquals(toStringAndClose(object.getPayload().openStream()), "swifty");
      }
   }

   public void testPrivateByDefault() throws Exception {
      for (String regionId : regions) {
         SwiftObject object = getApi().getObjectApi(regionId, containerName).get(name);
         try {
            object.getUri().toURL().openStream();
            fail("shouldn't be able to access " + object);
         } catch (IOException expected) {
         }
      }
   }

   public void testGetOptions() throws Exception {
      for (String regionId : regions) {
         SwiftObject object = getApi().getObjectApi(regionId, containerName).get(name, tail(1));
         assertEquals(object.getName(), name);
         checkObject(object);
         assertEquals(toStringAndClose(object.getPayload().openStream()), "y");
      }
   }

   public void testListOptions() throws Exception {
      String lexicographicallyBeforeName = name.substring(0, name.length() - 1);
      for (String regionId : regions) {
         SwiftObject object = getApi().getObjectApi(regionId, containerName)
               .list(marker(lexicographicallyBeforeName)).get(0);
         assertEquals(object.getName(), name);
         checkObject(object);
      }
   }

   public void testDeleteMetadata() throws Exception {
      for (String regionId : regions) {
         ObjectApi objectApi = getApi().getObjectApi(regionId, containerName);

         Map<String, String> meta = ImmutableMap.of("MyDelete1", "foo", "MyDelete2", "bar");

         objectApi.updateMetadata(name, meta);
         assertFalse(objectApi.get(name).getMetadata().isEmpty());

         assertTrue(objectApi.deleteMetadata(name, meta));
         assertTrue(objectApi.get(name).getMetadata().isEmpty());
      }
   }

   @Override
   @BeforeClass(groups = "live")
   public void setup() {
      super.setup();
      for (String regionId : regions) {
         getApi().getContainerApi(regionId).create(containerName);
         getApi().getObjectApi(regionId, containerName).put(name, PAYLOAD);
      }
   }

   @AfterClass(groups = "live")
   public void tearDown() {
      for (String regionId : regions) {
         deleteAllObjectsInContainer(regionId, containerName);
         getApi().getObjectApi(regionId, containerName).delete(name);
         getApi().getContainerApi(regionId).deleteIfEmpty(containerName);
      }
   }

   static void checkObject(SwiftObject object) {
      assertNotNull(object.getName());
      assertNotNull(object.getUri());
      assertNotNull(object.getETag());
      assertTrue(object.getLastModified().getTime() <= System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
      assertNotNull(object.getPayload().getContentMetadata().getContentLength());
      assertNotNull(object.getPayload().getContentMetadata().getContentType());
   }
}
