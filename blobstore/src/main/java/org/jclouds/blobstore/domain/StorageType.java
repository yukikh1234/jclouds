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

public enum StorageType {

   /**
    * A container
    */
   CONTAINER,
   /**
    * An object in the object store
    */
   BLOB,
   /**
    * Represents "special" blobs that have content-type set to
    * application/directory.
    */
   FOLDER,
   /**
    * A partial path; used when the delimiter is set and represents all objects
    * that start with the same name up to the delimiter character (e.g. foo-bar
    * and foo-baz, with delimiter set to "-" will be returned as "foo-").
    */
   RELATIVE_PATH;

}
