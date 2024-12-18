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
package org.jclouds.glacier.functions;

import org.jclouds.glacier.reference.GlacierHeaders;
import org.jclouds.http.HttpException;
import org.jclouds.http.HttpResponse;

import com.google.common.base.Function;
import com.google.common.hash.HashCode;

/**
 * Parses the tree hash header from the HttpResponse.
 */
public class ParseMultipartUploadTreeHashHeader implements Function<HttpResponse, HashCode> {
   @Override
   public HashCode apply(HttpResponse from) {
      String treehash = from.getFirstHeaderOrNull(GlacierHeaders.TREE_HASH);
      if (treehash == null)
         throw new HttpException("Did not receive Tree hash");
      return HashCode.fromString(treehash);
   }
}
