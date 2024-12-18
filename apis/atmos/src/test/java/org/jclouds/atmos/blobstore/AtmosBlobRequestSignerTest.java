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
package org.jclouds.atmos.blobstore;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;

import org.jclouds.apis.ApiMetadata;
import org.jclouds.atmos.AtmosApiMetadata;
import org.jclouds.atmos.AtmosClient;
import org.jclouds.atmos.config.AtmosHttpApiModule;
import org.jclouds.atmos.filters.SignRequest;
import org.jclouds.blobstore.BlobRequestSigner;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.Blob.Factory;
import org.jclouds.date.TimeStamp;
import org.jclouds.http.HttpRequest;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.internal.BaseRestAnnotationProcessingTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Supplier;
import com.google.common.hash.HashCode;
import com.google.inject.Module;

/**
 * Tests behavior of {@code AtmosBlobRequestSigner}
 */
// NOTE:without testName, this will not call @Before* and fail w/NPE during surefire
@Test(groups = "unit", testName = "AtmosBlobRequestSignerTest")
public class AtmosBlobRequestSignerTest extends BaseRestAnnotationProcessingTest<AtmosClient> {

   public AtmosBlobRequestSignerTest() {
      // this is base64 decoded in the signer;
      credential = "aaaabbbb"; 
   }
   
   private BlobRequestSigner signer;
   private Factory blobFactory;

   public void testSignGetBlob() throws ArrayIndexOutOfBoundsException, SecurityException, IllegalArgumentException,
            NoSuchMethodException, IOException {
      HttpRequest request = signer.signGetBlob("container", "name");

      assertRequestLineEquals(request, "GET https://accesspoint.atmosonline.com/rest/namespace/container/name?uid=identity&expires=1212684799&signature=oijXdvPjHQ/LwWDcdx9Eozsu77o%3D HTTP/1.1");
      assertNonPayloadHeadersEqual(request, "");
      assertPayloadEquals(request, null, null, false);

      assertEquals(request.getFilters().size(), 0);
   }

   public void testSignPutBlob() throws ArrayIndexOutOfBoundsException, SecurityException, IllegalArgumentException,
            NoSuchMethodException, IOException {
      HashCode hashCode = HashCode.fromBytes(new byte[16]);
      Blob blob = blobFactory.create(null);
      blob.getMetadata().setName("name");
      blob.setPayload("");
      blob.getPayload().getContentMetadata().setContentLength(2L);
      blob.getPayload().getContentMetadata().setContentMD5(hashCode.asBytes());
      blob.getPayload().getContentMetadata().setContentType("text/plain");
      blob.getPayload().getContentMetadata().setExpires(new Date(1000));
      
      HttpRequest request = signer.signPutBlob("container", blob);

      assertRequestLineEquals(request,
               "POST https://accesspoint.atmosonline.com/rest/namespace/container/name HTTP/1.1");
      assertNonPayloadHeadersEqual(
               request,
               "Accept: */*\n" +
               "Date: Thu, 05 Jun 2008 16:38:19 GMT\n" +
               "Expect: 100-continue\n" +
               "x-emc-signature: OlAHsoIDCsO5YmqjRxOIM5sp3r0=\n" +
               "x-emc-uid: identity\n" +
               "x-emc-wschecksum: MD5/0/00000000000000000000000000000000\n");

      assertContentHeadersEqual(request, "text/plain", null, null, null, 2L, hashCode.asBytes(), new Date(1000));

      assertEquals(request.getFilters().size(), 0);
   }

   @BeforeClass
   protected void setupFactory() throws IOException {
      super.setupFactory();
      this.blobFactory = injector.getInstance(Blob.Factory.class);
      this.signer = injector.getInstance(BlobRequestSigner.class);
   }

   @Override
   protected void checkFilters(HttpRequest request) {
      assertEquals(request.getFilters().size(), 1);
      assertEquals(request.getFilters().get(0).getClass(), SignRequest.class);
   }

   @Override
   protected Module createModule() {
      return new TestAtmosHttpApiModule();
   }

      @ConfiguresHttpApi
   private static final class TestAtmosHttpApiModule extends AtmosHttpApiModule {
      @Override
      protected void configure() {
         super.configure();
      }

      @Override
      protected String provideTimeStamp(@TimeStamp Supplier<String> cache) {
         return "Thu, 05 Jun 2008 16:38:19 GMT";
      }
   }

   @Override
   public ApiMetadata createApiMetadata() {
      return new AtmosApiMetadata();
   }

}
