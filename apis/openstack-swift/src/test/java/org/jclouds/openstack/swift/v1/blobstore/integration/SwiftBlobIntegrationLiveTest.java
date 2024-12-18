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
package org.jclouds.openstack.swift.v1.blobstore.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.CREDENTIAL_TYPE;

import java.util.Properties;

import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.Tier;
import org.jclouds.blobstore.integration.internal.BaseBlobIntegrationTest;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = "live", testName = "SwiftBlobIntegrationLiveTest")
public class SwiftBlobIntegrationLiveTest extends BaseBlobIntegrationTest {

   public SwiftBlobIntegrationLiveTest() {
      provider = "openstack-swift";
   }

   @Override
   protected Properties setupProperties() {
      Properties props = super.setupProperties();
      setIfTestSystemPropertyPresent(props, CREDENTIAL_TYPE);
      return props;
   }

   // Object/Container name contains forbidden chars from "<>
   @Override
   @DataProvider(name = "delete")
   public Object[][] createData() {
      return new Object[][] { { "normal" }, { "sp ace" }, { "qu?stion" }, { "unic₪de" }, { "path/foo" }, { "colon:" },
            { "asteri*k" }, { "p|pe" } };
   }

   @Override
   public void testGetTwoRanges() {
      throw new SkipException("unsupported in swift");
   }

   @Override
   public void testCreateBlobWithExpiry() throws InterruptedException {
      throw new SkipException("unsupported in swift");
   }

   @Test(groups = { "integration", "live" })
   public void testGetIfUnmodifiedSince() throws InterruptedException {
      throw new SkipException("unsupported in swift");
   }

   @Override
   protected int getIncorrectContentMD5StatusCode() {
      return 422;
   }

   // not supported
   @Override
   protected void checkCacheControl(Blob blob, String cacheControl) {
      assertThat(blob.getPayload().getContentMetadata().getCacheControl()).isNull();
      assertThat(blob.getMetadata().getContentMetadata().getCacheControl()).isNull();
   }

   // not supported
   @Override
   protected void checkContentLanguage(Blob blob, String contentLanguage) {
      assert blob.getPayload().getContentMetadata().getContentLanguage() == null;
      assert blob.getMetadata().getContentMetadata().getContentLanguage() == null;
   }

   @Override
   public void testSetBlobAccess() throws Exception {
      throw new SkipException("unsupported in swift");
   }

   @Override
   @Test(expectedExceptions = UnsupportedOperationException.class)
   public void testPutBlobAccess() throws Exception {
      super.testPutBlobAccess();
   }

   @Override
   @Test(expectedExceptions = UnsupportedOperationException.class)
   public void testPutBlobAccessMultipart() throws Exception {
      super.testPutBlobAccessMultipart();
   }

   @Override
   @Test(expectedExceptions = UnsupportedOperationException.class)
   public void testCopyIfNoneMatch() throws Exception {
      super.testCopyIfNoneMatch();
   }

   @Override
   @Test(expectedExceptions = UnsupportedOperationException.class)
   public void testCopyIfNoneMatchNegative() throws Exception {
      super.testCopyIfNoneMatchNegative();
   }

   @Override
   public void testListMultipartUploads() throws Exception {
      try {
         super.testListMultipartUploads();
         failBecauseExceptionWasNotThrown(UnsupportedOperationException.class);
      } catch (UnsupportedOperationException uoe) {
         throw new SkipException("Swift does not support listing multipart uploads", uoe);
      }
   }

   // TODO: testCopyIfModifiedSinceNegative throws HTTP 304 not 412 error

   @Override
   protected long getMinimumMultipartBlobSize() {
      return 1;
   }

   @Override
   protected void checkTier(BlobMetadata metadata, Tier expected) {
      // Swift maps all tiers to STANDARD
      assertThat(metadata.getTier()).isEqualTo(Tier.STANDARD);
   }
}
