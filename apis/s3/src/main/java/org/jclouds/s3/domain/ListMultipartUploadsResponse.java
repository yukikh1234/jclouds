
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
package org.jclouds.s3.domain;

import java.util.Date;
import java.util.List;

import org.jclouds.javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class ListMultipartUploadsResponse {
   public abstract String bucket();
   @Nullable public abstract String keyMarker();
   @Nullable public abstract String uploadIdMarker();
   @Nullable public abstract String nextKeyMarker();
   @Nullable public abstract String nextUploadIdMarker();
   public abstract int maxUploads();
   public abstract boolean isTruncated();
   public abstract List<Upload> uploads();

   public static ListMultipartUploadsResponse create(
         String bucket,
         @Nullable String keyMarker,
         @Nullable String uploadIdMarker,
         @Nullable String nextKeyMarker,
         @Nullable String nextUploadIdMarker,
         int maxUploads,
         boolean isTruncated,
         List<Upload> uploads) {
      validateInputs(bucket, maxUploads, uploads);
      uploads = ImmutableList.copyOf(uploads);
      return new AutoValue_ListMultipartUploadsResponse(bucket, keyMarker, uploadIdMarker, nextKeyMarker, nextUploadIdMarker, maxUploads, isTruncated, uploads);
   }

   private static void validateInputs(String bucket, int maxUploads, List<Upload> uploads) {
      if (bucket == null || bucket.isEmpty()) {
         throw new IllegalArgumentException("Bucket name cannot be null or empty");
      }
      if (maxUploads < 0) {
         throw new IllegalArgumentException("maxUploads cannot be negative");
      }
      if (uploads == null) {
         throw new IllegalArgumentException("Uploads list cannot be null");
      }
   }

   @AutoValue
   public abstract static class Upload {
      public abstract String key();
      public abstract String uploadId();
      public abstract CanonicalUser initiator();
      public abstract CanonicalUser owner();
      public abstract ObjectMetadata.StorageClass storageClass();
      public abstract Date initiated();

      public static Upload create(
            String key,
            String uploadId,
            CanonicalUser initiator,
            CanonicalUser owner,
            ObjectMetadata.StorageClass storageClass,
            Date initiated) {
         validateUploadInputs(key, uploadId, initiator, owner, storageClass, initiated);
         initiated = (Date) initiated.clone();
         return new AutoValue_ListMultipartUploadsResponse_Upload(key, uploadId, initiator, owner, storageClass, initiated);
      }

      private static void validateUploadInputs(
            String key,
            String uploadId,
            CanonicalUser initiator,
            CanonicalUser owner,
            ObjectMetadata.StorageClass storageClass,
            Date initiated) {
         if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
         }
         if (uploadId == null || uploadId.isEmpty()) {
            throw new IllegalArgumentException("UploadId cannot be null or empty");
         }
         if (initiator == null) {
            throw new IllegalArgumentException("Initiator cannot be null");
         }
         if (owner == null) {
            throw new IllegalArgumentException("Owner cannot be null");
         }
         if (storageClass == null) {
            throw new IllegalArgumentException("StorageClass cannot be null");
         }
         if (initiated == null) {
            throw new IllegalArgumentException("Initiated date cannot be null");
         }
      }
   }
}
