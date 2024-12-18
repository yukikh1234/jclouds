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
package org.jclouds.compute.domain;

import org.jclouds.compute.config.CustomizationResponse;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class ExecResponse implements CustomizationResponse {

   public static final int DEFAULT_EXIT_STATUS = -1;
    
   private final String output;
   private final String error;
   private final int exitStatus;

   public ExecResponse(String output, String error, Integer exitStatus) {
      this.output = output;
      this.error = error;
      this.exitStatus = exitStatus != null ? exitStatus : DEFAULT_EXIT_STATUS;
   }

   public String getError() {
      return error;
   }

   public String getOutput() {
      return output;
   }

   public int getExitStatus() {
      return exitStatus;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(output, error, exitStatus);
   }

   @Override
   public boolean equals(Object o) {
      if (o == null)
         return false;
      if (!o.getClass().equals(getClass()))
         return false;
      ExecResponse that = ExecResponse.class.cast(o);
      return Objects.equal(this.output, that.output) && Objects.equal(this.error, that.error)
            && Objects.equal(this.exitStatus, that.exitStatus);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper("").add("output", output).add("error", error).add("exitStatus", exitStatus)
            .toString();
   }

}
