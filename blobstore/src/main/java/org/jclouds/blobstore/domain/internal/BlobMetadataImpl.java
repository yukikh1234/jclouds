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
package org.jclouds.blobstore.domain.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.util.Date;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;

import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.domain.Tier;
import org.jclouds.domain.Location;
import org.jclouds.io.ContentMetadata;
import org.jclouds.javax.annotation.Nullable;

/**
 * System and user Metadata for the {@link Blob}.
 */
public class BlobMetadataImpl extends StorageMetadataImpl implements BlobMetadata {

   private final URI publicUri;
   private final String container;
   private final ContentMetadata contentMetadata;

   public BlobMetadataImpl(String id, String name, @Nullable Location location, URI uri, String eTag,
            @Nullable Date creationDate, @Nullable Date lastModified,
            Map<String, String> userMetadata, @Nullable URI publicUri,
            @Nullable String container, ContentMetadata contentMetadata, @Nullable Long size,
            Tier tier) {
      super(StorageType.BLOB, id, name, location, uri, eTag, creationDate, lastModified, userMetadata, size, tier);
      this.publicUri = publicUri;
      this.container = container;
      this.contentMetadata = checkNotNull(contentMetadata, "contentMetadata");
   }

   @Deprecated
   public BlobMetadataImpl(String id, String name, @Nullable Location location, URI uri, String eTag,
            @Nullable Date creationDate, @Nullable Date lastModified,
            Map<String, String> userMetadata, @Nullable URI publicUri,
            @Nullable String container, ContentMetadata contentMetadata, @Nullable Long size) {
      this(id, name, location, uri, eTag, creationDate, lastModified, userMetadata, publicUri, container, contentMetadata, size, Tier.STANDARD);
   }

   @Deprecated
   public BlobMetadataImpl(String id, String name, @Nullable Location location, URI uri, String eTag,
            @Nullable Date creationDate, @Nullable Date lastModified,
            Map<String, String> userMetadata, @Nullable URI publicUri,
            @Nullable String container, ContentMetadata contentMetadata) {
      this(id, name, location, uri, eTag, creationDate, lastModified, userMetadata, publicUri, container, contentMetadata, null);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public URI getPublicUri() {
      return publicUri;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getContainer() {
      return container;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ContentMetadata getContentMetadata() {
      return contentMetadata;
   }

   @Override
   public boolean equals(Object object) {
      if (object == this) {
         return true;
      }
      if (!(object instanceof BlobMetadataImpl)) {
         return false;
      }
      BlobMetadataImpl that = (BlobMetadataImpl) object;
      return super.equals(that) &&
            Objects.equal(publicUri, that.publicUri) &&
            Objects.equal(container, that.container) &&
            Objects.equal(contentMetadata, that.contentMetadata);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(super.hashCode(), publicUri, container, contentMetadata);
   }

   @Override
   protected ToStringHelper string() {
      return super.string()
            .add("publicUri", publicUri)
            .add("container", container)
            .add("contentMetadata", contentMetadata);
   }
}
