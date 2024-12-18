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
package org.jclouds.domain.internal;

import java.net.URI;
import java.util.Map;

import org.jclouds.domain.Location;
import org.jclouds.domain.MutableResourceMetadata;
import org.jclouds.domain.ResourceMetadata;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Maps;

/**
 * Used to construct new resources or modify existing ones.
 */
public class MutableResourceMetadataImpl<T extends Enum<T>> implements MutableResourceMetadata<T> {

   private T type;
   private String id;
   private String name;
   private Location location;
   private URI uri;
   private Map<String, String> userMetadata;

   public MutableResourceMetadataImpl() {
      userMetadata = Maps.newLinkedHashMap();
   }

   public MutableResourceMetadataImpl(ResourceMetadata<T> from) {
      this.type = from.getType();
      this.id = from.getProviderId();
      this.name = from.getName();
      this.location = from.getLocation();
      this.uri = from.getUri();
      this.userMetadata = from.getUserMetadata();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int compareTo(ResourceMetadata<T> o) {
      if (getName() == null)
         return -1;
      return (this == o) ? 0 : getName().compareTo(o.getName());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public T getType() {
      return type;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName() {
      return name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getProviderId() {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public URI getUri() {
      return uri;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<String, String> getUserMetadata() {
      return userMetadata;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setName(String name) {
      this.name = name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setType(T type) {
      this.type = type;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setUserMetadata(Map<String, String> userMetadata) {
      this.userMetadata = userMetadata;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setId(String id) {
      this.id = id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setUri(URI uri) {
      this.uri = uri;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setLocation(Location location) {
      this.location = location;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Location getLocation() {
      return location;
   }

   @Override
   public String toString() {
      return string().toString();
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper("").omitNullValues()
            .add("id", id)
            .add("location", location)
            .add("name", name)
            .add("type", getType())
            .add("uri", uri)
            .add("userMetadata", userMetadata);
   }

   @Override
   public int hashCode() {
      // intentionally not hashing userMetadata
      return Objects.hashCode(id, location, name, type, uri);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (!(obj instanceof MutableResourceMetadata<?>))
         return false;
      MutableResourceMetadata<?> other = (MutableResourceMetadata<?>) obj;
      return Objects.equal(id, other.getProviderId())
            && Objects.equal(location, other.getLocation())
            && Objects.equal(name, other.getName())
            && Objects.equal(type, other.getType())
            && Objects.equal(uri, other.getUri());
            // intentionally not comparing userMetadata
   }

}
