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
package org.jclouds.azurecompute.arm.domain;

import static org.jclouds.azurecompute.arm.domain.IdReference.extractName;
import static org.jclouds.azurecompute.arm.domain.IdReference.extractResourceGroup;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class ImageReference {

   /**
    * Specifies the resource identifier of a virtual machine image in your subscription. This element is only used
    * for virtual machine images, not platform images or marketplace images.
    */
   @Nullable
   public abstract String customImageId();

   /**
    * The publisher of the image reference.
    */
   @Nullable
   public abstract String publisher();

   /**
    * The offer of the image reference.
    */
   @Nullable
   public abstract String offer();

   /**
    * The sku of the image reference.
    */
   @Nullable
   public abstract String sku();

   /**
    * The version of the image reference.
    */
   @Nullable
   public abstract String version();
   
   ImageReference() {
      
   }

   @SerializedNames({"id", "publisher", "offer", "sku", "version"})
   public static ImageReference create(final String id,
                                       final String publisher,
                                       final String offer,
                                       final String sku,
                                       final String version) {

      return builder().customImageId(id)
              .publisher(publisher)
              .offer(offer)
              .sku(sku)
              .version(version)
              .build();
   }
   
   public abstract Builder toBuilder();

   public static Builder builder() {
      return new AutoValue_ImageReference.Builder();
   }

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder customImageId(String ids);
      public abstract Builder publisher(String publisher);
      public abstract Builder offer(String offer);
      public abstract Builder sku(String sku);
      public abstract Builder version(String version);

      public abstract ImageReference build();
   }
   
   public String encodeFieldsToUniqueId(String location) {
      return VMImage.azureImage().location(location).publisher(publisher()).offer(offer()).sku(sku()).build()
            .encodeFieldsToUniqueId();
   }

   public String encodeFieldsToUniqueIdCustom(String location) {
      return VMImage.customImage().resourceGroup(extractResourceGroup(customImageId())).location(location)
            .name(extractName(customImageId())).build().encodeFieldsToUniqueIdCustom();
   }
}
