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
package org.jclouds.azurecompute.arm.domain;

import java.util.List;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

@AutoValue
public abstract class NetworkInterfaceCardProperties implements Provisionable {

   @Nullable public abstract String provisioningState();
   @Nullable public abstract String resourceGuid();
   @Nullable public abstract Boolean enableIPForwarding();
   @Nullable public abstract List<IpConfiguration> ipConfigurations();
   @Nullable public abstract IdReference networkSecurityGroup();
   @Nullable public abstract IdReference virtualMachine();
   @Nullable public abstract Boolean primary();

   @SerializedNames({"provisioningState", "resourceGuid", "enableIPForwarding", "ipConfigurations",
         "networkSecurityGroup", "virtualMachine", "primary" })
   public static NetworkInterfaceCardProperties create(final String provisioningState, final String resourceGuid,
         final Boolean enableIPForwarding, final List<IpConfiguration> ipConfigurations,
         final IdReference networkSecurityGroup, final IdReference virtualMachine, final Boolean primary) {
      NetworkInterfaceCardProperties.Builder builder = NetworkInterfaceCardProperties.builder()
              .provisioningState(provisioningState)
              .resourceGuid(resourceGuid)
              .enableIPForwarding(enableIPForwarding)
              .ipConfigurations(ipConfigurations == null ? null : ImmutableList.copyOf(ipConfigurations))
            .networkSecurityGroup(networkSecurityGroup).virtualMachine(virtualMachine).primary(primary);

      return builder.build();
   }

   NetworkInterfaceCardProperties() {

   }

   public abstract Builder toBuilder();

   public static Builder builder() {
      return new AutoValue_NetworkInterfaceCardProperties.Builder();
   }

   @AutoValue.Builder
   public abstract static class Builder {
      public abstract Builder provisioningState(String provisioningState);
      public abstract Builder resourceGuid(String resourceGuid);
      public abstract Builder enableIPForwarding(Boolean enableIPForwarding);
      public abstract Builder ipConfigurations(List<IpConfiguration> ipConfigurations);
      public abstract Builder networkSecurityGroup(IdReference networkSecurityGroup);
      public abstract Builder virtualMachine(IdReference virtualMachine);
      public abstract Builder primary(Boolean primary);


      abstract List<IpConfiguration> ipConfigurations();
      abstract NetworkInterfaceCardProperties autoBuild();

      public NetworkInterfaceCardProperties build() {
         ipConfigurations(ipConfigurations() != null ? ImmutableList.copyOf(ipConfigurations()) : null);
         return autoBuild();
      }
   }
}


