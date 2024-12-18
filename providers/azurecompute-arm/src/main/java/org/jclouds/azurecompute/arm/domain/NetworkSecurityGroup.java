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

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import java.util.Map;

@AutoValue
public abstract class NetworkSecurityGroup {
   public abstract String id();
   public abstract String name();

   @Nullable
   public abstract String location();

   @Nullable
   public abstract Map<String, String> tags();

   @Nullable
   public abstract NetworkSecurityGroupProperties properties();

   @Nullable
   public abstract String etag();

   @SerializedNames({ "id", "name", "location", "tags", "properties", "etag" })
   public static NetworkSecurityGroup create(final String id, final String name, final String location,
         final Map<String, String> tags, final NetworkSecurityGroupProperties properties, final String etag) {
      return new AutoValue_NetworkSecurityGroup(id, name, location, (tags == null) ? null : ImmutableMap.copyOf(tags),
            properties, etag);
   }
}
