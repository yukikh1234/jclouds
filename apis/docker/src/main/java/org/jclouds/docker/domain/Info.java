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
package org.jclouds.docker.domain;

import java.util.List;

import org.jclouds.javax.annotation.Nullable;
import org.jclouds.json.SerializedNames;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Info {

   public abstract int containers();

   public abstract boolean debug();

   public abstract String driver();

   public abstract List<List<String>> driverStatus();

   public abstract String executionDriver();

   public abstract boolean iPv4Forwarding();

   public abstract int images();

   public abstract String indexServerAddress();

   @Nullable public abstract String initPath();

   @Nullable public abstract String initSha1();

   public abstract String kernelVersion();

   public abstract boolean memoryLimit();

   public abstract int nEventsListener();

   public abstract int nFd();

   public abstract int nGoroutines();

   public abstract String operatingSystem();

   public abstract boolean swapLimit();

   public abstract String dockerRootDir();

   /**
    * @return a list of daemon labels
    */
   @Nullable public abstract List<String> labels();

   /**
    * @return total memory available
    */
   public abstract long memTotal();

   /**
    * @return the number of CPUs available on the machine
    */
   public abstract int ncpu();

   /**
    * @return a unique ID identifying the daemon
    */
   public abstract String id();

   /**
    * @return a user-friendly name describing the running Docker daemon
    */
   public abstract String name();

   Info() {
   }

   @SerializedNames({
                   "Containers", "Debug", "Driver", "DriverStatus", "ExecutionDriver", "IPv4Forwarding", "Images",
                   "IndexServerAddress", "InitPath", "InitSha1", "KernelVersion", "MemoryLimit", "NEventsListener",
                   "NFd", "NGoroutines", "OperatingSystem", "SwapLimit", "DockerRootDir", "Labels", "MemTotal", "NCPU",
                   "ID", "Name"
   })
   public static Info create(int containers, boolean debug, String driver, List<List<String>> driverStatus,
                             String executionDriver, boolean iPv4Forwarding, int images, String indexServerAddress,
                             String initPath, String initSha1, String kernelVersion, boolean memoryLimit,
                             int nEventsListener, int nFd, int nGoroutines, String operatingSystem, boolean swapLimit,
                             String dockerRootDir, List<String> labels, long memTotal, int ncpu, String id, String name) {
      return new AutoValue_Info(containers, debug, driver, driverStatus, executionDriver, iPv4Forwarding, images,
              indexServerAddress, initPath, initSha1, kernelVersion, memoryLimit, nEventsListener, nFd, nGoroutines,
              operatingSystem, swapLimit, dockerRootDir, labels, memTotal, ncpu, id, name);
   }
}
