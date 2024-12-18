
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
package org.jclouds.s3.blobstore.strategy;

public final class MultipartUpload {

   /* Maximum number of parts per upload */
   private static final int MAX_NUMBER_OF_PARTS = 10000;
   /* Maximum number of parts returned for a list parts request */
   private static final int MAX_LIST_PARTS_RETURNED = 1000;
   /* Maximum number of multipart uploads returned in a list multipart uploads request */
   private static final int MAX_LIST_MPU_RETURNED = 1000;

   /*
    * part size 5 MB to 5 GB, last part can be < 5 MB
    */
   private static final long MIN_PART_SIZE = 5242880L;
   private static final long MAX_PART_SIZE = 5368709120L;

   private MultipartUpload() {
      // Prevent instantiation
   }

   public static int getMaxNumberOfParts() {
       return MAX_NUMBER_OF_PARTS;
   }

   public static int getMaxListPartsReturned() {
       return MAX_LIST_PARTS_RETURNED;
   }

   public static int getMaxListMpuReturned() {
       return MAX_LIST_MPU_RETURNED;
   }

   public static long getMinPartSize() {
       return MIN_PART_SIZE;
   }

   public static long getMaxPartSize() {
       return MAX_PART_SIZE;
   }
}
