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
import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

/**
 * Offer
 */
@AutoValue
public abstract class Offer {
   /**
    * The location of the Offer
    */
   @Nullable
   public abstract String location();

   /**
    * The name of the Offer
    */
   @Nullable
   public abstract String name();

   /**
    * The id of the Offer
    */
   @Nullable
   public abstract String id();

   @SerializedNames({"location", "name", "id"})
   public static Offer create(final String location, final String name, final String id) {

      return new AutoValue_Offer(location, name, id);
   }
}
