
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
package org.jclouds.s3.functions;

import jakarta.inject.Singleton;
import org.jclouds.s3.domain.ObjectMetadata;
import com.google.common.base.Function;

@Singleton
public class ObjectMetadataKey implements Function<Object, String> {

   @Override
   public String apply(Object from) {
      if (from instanceof ObjectMetadata) {
         return ((ObjectMetadata) from).getKey();
      }
      throw new IllegalArgumentException("Invalid input type. Expected ObjectMetadata.");
   }
}
