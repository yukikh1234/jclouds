
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.http.HttpResponse;
import org.jclouds.http.functions.ReturnStringIf2xx;

import com.google.common.base.Function;

@Singleton
public class UploadIdFromHttpResponseViaRegex implements Function<HttpResponse, String> {
   private static final Pattern UPLOAD_ID_PATTERN = Pattern.compile("<UploadId>([\\S&&[^<]]+)</UploadId>");
   private final ReturnStringIf2xx returnStringIf200;

   @Inject
   public UploadIdFromHttpResponseViaRegex(ReturnStringIf2xx returnStringIf200) {
      this.returnStringIf200 = returnStringIf200;
   }

   @Override
   public String apply(HttpResponse response) {
      String content = returnStringIf200.apply(response);
      return extractUploadId(content);
   }

   /**
    * Extracts the UploadId from the given content string using regex.
    * 
    * @param content the content string to search for an UploadId.
    * @return the extracted UploadId or null if none is found.
    */
   private String extractUploadId(String content) {
      if (content != null) {
         Matcher matcher = UPLOAD_ID_PATTERN.matcher(content);
         if (matcher.find()) {
            return matcher.group(1);
         }
      }
      return null;
   }
}
