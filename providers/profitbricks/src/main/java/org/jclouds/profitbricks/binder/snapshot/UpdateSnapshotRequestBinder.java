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
package org.jclouds.profitbricks.binder.snapshot;

import static java.lang.String.format;
import org.jclouds.profitbricks.binder.BaseProfitBricksRequestBinder;
import org.jclouds.profitbricks.domain.Snapshot;

public class UpdateSnapshotRequestBinder extends BaseProfitBricksRequestBinder<Snapshot.Request.UpdatePayload> {

   protected final StringBuilder requestBuilder;

   protected UpdateSnapshotRequestBinder() {
      super("snapshot");
      this.requestBuilder = new StringBuilder(128);
   }

   @Override
   protected String createPayload(Snapshot.Request.UpdatePayload payload) {
      requestBuilder.append("<ws:updateSnapshot>")
              .append("<request>")
              .append(format("<snapshotId>%s</snapshotId>", payload.id()))
              .append(format("<description>%s</description>", payload.description()))
              .append(format("<snapshotName>%s</snapshotName>", payload.name()))
              .append(formatIfNotEmpty("<bootable>%s</bootable>", payload.bootable()))
              .append(formatIfNotEmpty("<osType>%s</osType>", payload.osType()))
              .append(formatIfNotEmpty("<cpuHotPlug>%s</cpuHotPlug>", payload.isCpuHotPlug()))
              .append(formatIfNotEmpty("<cpuHotUnPlug>%s</cpuHotUnPlug>", payload.isCpuHotUnPlug()))
              .append(formatIfNotEmpty("<ramHotPlug>%s</ramHotPlug>", payload.isRamHotPlug()))
              .append(formatIfNotEmpty("<ramHotUnPlug>%s</ramHotUnPlug>", payload.isRamHotUnPlug()))
              .append(formatIfNotEmpty("<nicHotPlug>%s</nicHotPlug>", payload.isNicHotPlug()))
              .append(formatIfNotEmpty("<nicHotUnPlug>%s</nicHotUnPlug>", payload.isNicHotUnPlug()))
              .append(formatIfNotEmpty("<discVirtioHotPlug>%s</discVirtioHotPlug>", payload.isDiscVirtioHotPlug()))
              .append(formatIfNotEmpty("<discVirtioHotUnPlug>%s</discVirtioHotUnPlug>", payload.isDiscVirtioHotUnPlug()))
              .append("</request>")
              .append("</ws:updateSnapshot>");
      return requestBuilder.toString();
   }
}
