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
package org.jclouds.b2.domain;

import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Authorization {
   public abstract String accountId();
   public abstract String apiUrl();
   public abstract String authorizationToken();
   public abstract String downloadUrl();
   public abstract long absoluteMinimumPartSize();
   public abstract long recommendedPartSize();

   @SerializedNames({"accountId", "apiUrl", "authorizationToken", "downloadUrl", "absoluteMinimumPartSize", "recommendedPartSize"})
   public static Authorization create(String accountId, String apiUrl, String authorizationToken, String downloadUrl, long absoluteMinimumPartSize, long recommendedPartSize) {
      return new AutoValue_Authorization(accountId, apiUrl, authorizationToken, downloadUrl, absoluteMinimumPartSize, recommendedPartSize);
   }
}
