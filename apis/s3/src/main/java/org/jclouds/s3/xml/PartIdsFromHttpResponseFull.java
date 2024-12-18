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
package org.jclouds.s3.xml;

import static org.jclouds.util.SaxUtils.currentOrNull;

import java.util.Date;
import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.date.DateService;
import org.jclouds.http.functions.ParseSax;
import org.jclouds.s3.domain.ListMultipartUploadResponse;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableMap;

/**
 * Parses the following XML document:
 * <p/>
 * ListBucketResult xmlns="http://s3.amazonaws.com/doc/2006-03-01"
 */
@Beta
public final class PartIdsFromHttpResponseFull extends ParseSax.HandlerWithResult<Map<Integer, ListMultipartUploadResponse>> {
   private final StringBuilder currentText = new StringBuilder();

   private final DateService dateParser;

   private int partNumber;
   private Date lastModfied;
   private String eTag;
   private long size;

   private final ImmutableMap.Builder<Integer, ListMultipartUploadResponse> parts = ImmutableMap.builder();

   @Inject
   PartIdsFromHttpResponseFull(DateService dateParser) {
      this.dateParser = dateParser;
   }

   public Map<Integer, ListMultipartUploadResponse> getResult() {
      return parts.build();
   }

   public void endElement(String uri, String name, String qName) {
      if (qName.equals("PartNumber")) {
         partNumber = Integer.parseInt(currentText.toString().trim());
      } else if (qName.equals("LastModified")) {
         lastModfied = dateParser.iso8601DateOrSecondsDateParse(currentOrNull(currentText));
      } else if (qName.equals("ETag")) {
         eTag = currentText.toString().trim();
      } else if (qName.equals("Size")) {
         size = Long.parseLong(currentText.toString().trim());
      } else if (qName.equals("Part")) {
         parts.put(partNumber, ListMultipartUploadResponse.create(partNumber, lastModfied, eTag, size));
      }
      currentText.setLength(0);
   }

   public void characters(char[] ch, int start, int length) {
      currentText.append(ch, start, length);
   }
}

