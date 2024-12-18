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
package org.jclouds.openstack.swift.v1.blobstore.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import org.jclouds.blobstore.domain.MutableBlobMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.domain.Tier;
import org.jclouds.blobstore.domain.internal.MutableBlobMetadataImpl;
import org.jclouds.openstack.swift.v1.domain.Container;
import org.jclouds.openstack.swift.v1.domain.SwiftObject;
import org.jclouds.openstack.swift.v1.functions.ParseObjectListFromResponse;

import com.google.common.base.Function;

public class ToBlobMetadata implements Function<SwiftObject, MutableBlobMetadata> {

   private final Container container;

   public ToBlobMetadata(Container container) {
      this.container = checkNotNull(container, "container");
   }

   @Override
   public MutableBlobMetadata apply(SwiftObject from) {
      if (from == null)
         return null;
      MutableBlobMetadata to = new MutableBlobMetadataImpl();
      to.setContainer(container.getName());
      if (container.getAnybodyRead().isPresent()) {
         to.setPublicUri(from.getUri());
      }
      String eTag = from.getETag();
      to.setUri(from.getUri());
      to.setETag(eTag);
      to.setName(from.getName());
      to.setLastModified(from.getLastModified());
      to.setContentMetadata(from.getPayload().getContentMetadata());
      to.getContentMetadata().setContentMD5(from.getPayload().getContentMetadata().getContentMD5AsHashCode());
      to.getContentMetadata().setExpires(from.getPayload().getContentMetadata().getExpires());
      to.setUserMetadata(from.getMetadata());
      if (eTag != null && eTag.equals(ParseObjectListFromResponse.SUBDIR_ETAG)) {
         to.setType(StorageType.FOLDER);
      } else {
         to.setType(StorageType.BLOB);
      }
      to.setSize(from.getPayload().getContentMetadata().getContentLength());
      to.setTier(Tier.STANDARD);
      return to;
   }

   @Override
   public String toString() {
      return "ObjectToBlobMetadata(" + container + ")";
   }
}
