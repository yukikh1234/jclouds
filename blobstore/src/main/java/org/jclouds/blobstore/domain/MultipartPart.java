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

package org.jclouds.blobstore.domain;

import java.util.Date;

import org.jclouds.javax.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MultipartPart {
   public abstract int partNumber();
   public abstract long partSize();
   @Nullable public abstract String partETag();
   @Nullable public abstract Date lastModified();

   @Deprecated
   public static MultipartPart create(int partNumber, long partSize, @Nullable String partETag) {
      return MultipartPart.create(partNumber, partSize, partETag, null);
   }

   public static MultipartPart create(int partNumber, long partSize, @Nullable String partETag, @Nullable Date lastModified) {
      return new AutoValue_MultipartPart(partNumber, partSize, partETag, lastModified);
   }
}
