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
package org.jclouds.profitbricks.binder.nic;

import org.jclouds.profitbricks.binder.BaseProfitBricksRequestBinder;
import org.jclouds.profitbricks.domain.Nic;

import static java.lang.String.format;

public class SetInternetAccessBinder extends BaseProfitBricksRequestBinder<Nic.Request.SetInternetAccessPayload> {

   private final StringBuilder requestBuilder;

   SetInternetAccessBinder() {
      super("nic");
      this.requestBuilder = new StringBuilder(128);
   }

   @Override
   protected String createPayload(Nic.Request.SetInternetAccessPayload payload) {
      requestBuilder.append("<ws:setInternetAccess>")
              .append(format("<dataCenterId>%s</dataCenterId>", payload.dataCenterId()))
              .append(format("<lanId>%s</lanId>", payload.lanId()))
              .append(format("<internetAccess>%s</internetAccess>", payload.internetAccess()))
              .append("</ws:setInternetAccess>");
      return requestBuilder.toString();
   }
}
