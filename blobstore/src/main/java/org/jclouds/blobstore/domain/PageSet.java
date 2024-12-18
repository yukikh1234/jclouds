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

import java.util.Set;

public interface PageSet<T> extends Set<T> {

   /**
    * If there is a next marker, then the set is incomplete and you should issue another command to
    * retrieve the rest, setting the option {@code marker} to this value.  Some providers like Azure
    * have opaque markers so portable code should not rely on the actual values.
    * 
    * @return next marker, or null if list is complete
    */
   String getNextMarker();

}
