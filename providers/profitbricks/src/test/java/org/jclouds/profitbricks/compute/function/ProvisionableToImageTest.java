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
package org.jclouds.profitbricks.compute.function;

import static org.jclouds.profitbricks.domain.Location.US_LAS;
import static org.testng.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.ImageBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.profitbricks.domain.OsType;
import org.jclouds.profitbricks.domain.ProvisioningState;
import org.jclouds.profitbricks.domain.Snapshot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

@Test(groups = "unit", testName = "ProvisionableToImageTest")
public class ProvisionableToImageTest {

   private ProvisionableToImage fnImage;
   
   private final Location location = new LocationBuilder().id("us/las").description("us/las").scope(LocationScope.ZONE)
         .parent(new LocationBuilder().id("us").description("us").scope(LocationScope.REGION).build()).build();

   @BeforeTest
   public void setup() {
      this.fnImage = new ProvisionableToImage(Suppliers.<Set<? extends Location>> ofInstance(ImmutableSet.of(location)));
   }

   @Test
   public void testImageToImage() {
      org.jclouds.profitbricks.domain.Image image
              = org.jclouds.profitbricks.domain.Image.builder()
              .isBootable(true)
              .isCpuHotPlug(true)
              .isCpuHotUnPlug(false)
              .isDiscVirtioHotPlug(true)
              .isDiscVirtioHotUnPlug(true)
              .id("5ad99c9e-9166-11e4-9d74-52540066fee9")
              .name("Ubuntu-14.04-LTS-server-2015-01-01")
              .size(2048f)
              .type(org.jclouds.profitbricks.domain.Image.Type.HDD)
              .location(US_LAS)
              .isNicHotPlug(true)
              .isNicHotUnPlug(true)
              .osType(OsType.LINUX)
              .isPublic(true)
              .isRamHotPlug(true)
              .isRamHotUnPlug(false)
              .isWriteable(true)
              .build();

      Image actual = fnImage.apply(image);

      Image expected = new ImageBuilder()
              .ids(image.id())
              .name(image.name())
              .location(location)
              .status(Image.Status.AVAILABLE)
              .operatingSystem(OperatingSystem.builder()
                      .description("UBUNTU")
                      .family(OsFamily.UBUNTU)
                      .version("14.04")
                      .is64Bit(false)
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "image"))
              .build();

      assertEquals(actual, expected);
   }

   @Test
   public void testImageDescriptionParsing() {
      org.jclouds.profitbricks.domain.Image image1
              = org.jclouds.profitbricks.domain.Image.builder()
              .id("f4742db0-9160-11e4-9d74-52540066fee9")
              .name("Fedora-19-server-2015-01-01")
              .size(2048f)
              .type(org.jclouds.profitbricks.domain.Image.Type.HDD)
              .location(US_LAS)
              .osType(OsType.LINUX)
              .build();

      Image actual1 = fnImage.apply(image1);

      Image expected1 = new ImageBuilder()
              .ids(image1.id())
              .name(image1.name())
              .location(location)
              .status(Image.Status.AVAILABLE)
              .operatingSystem(OperatingSystem.builder()
                      .description("FEDORA")
                      .family(OsFamily.FEDORA)
                      .version("7")
                      .is64Bit(true)
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "image"))
              .build();

      assertEquals(actual1, expected1);

      org.jclouds.profitbricks.domain.Image image2
              = org.jclouds.profitbricks.domain.Image.builder()
              .id("457bf707-d5d1-11e3-8b4f-52540066fee9")
              .name("clearos-community-6.5.0-x86_64.iso")
              .size(2048f)
              .type(org.jclouds.profitbricks.domain.Image.Type.CDROM)
              .location(US_LAS)
              .osType(OsType.LINUX)
              .build();

      Image actual2 = fnImage.apply(image2);

      Image expected2 = new ImageBuilder()
              .ids(image2.id())
              .name(image2.name())
              .location(location)
              .status(Image.Status.AVAILABLE)
              .operatingSystem(OperatingSystem.builder()
                      .description("UNRECOGNIZED")
                      .family(OsFamily.UNRECOGNIZED)
                      .version("6.5.0")
                      .is64Bit(true)
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "image"))
              .build();

      assertEquals(actual2, expected2);

      org.jclouds.profitbricks.domain.Image image3
              = org.jclouds.profitbricks.domain.Image.builder()
              .id("e54af701-53b8-11e3-8f17-52540066fee9")
              .name("windows-2008-r2-server-setup.iso")
              .size(2048f)
              .type(org.jclouds.profitbricks.domain.Image.Type.CDROM)
              .location(US_LAS)
              .osType(OsType.WINDOWS)
              .build();

      Image actual3 = fnImage.apply(image3);

      Image expected3 = new ImageBuilder()
              .ids(image3.id())
              .name(image3.name())
              .location(location)
              .status(Image.Status.AVAILABLE)
              .operatingSystem(OperatingSystem.builder()
                      .description("WINDOWS")
                      .family(OsFamily.WINDOWS)
                      .version("2008")
                      .is64Bit(false)
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "image"))
              .build();

      assertEquals(actual3, expected3);

   }

   @Test
   public void testSnapshotToImage() {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2015, 4, 13);
      Date date = calendar.getTime();
      
      Snapshot snapshot1 = Snapshot.builder()
              .isBootable(true)
              .isCpuHotPlug(true)
              .isCpuHotUnPlug(false)
              .isDiscVirtioHotPlug(true)
              .isDiscVirtioHotUnPlug(true)
              .id("qswdefrg-qaws-qaws-defe-rgrgdsvcxbrh")
              .name("placeholder-snapshot-04/13/2015")
              .description("Created from \"placeholder\" in Data Center \"sbx-computeservice\"")
              .size(2048f)
              .location(US_LAS)
              .isNicHotPlug(true)
              .isNicHotUnPlug(true)
              .osType(OsType.LINUX)
              .isRamHotPlug(true)
              .isRamHotUnPlug(false)
              .creationTime(date)
              .lastModificationTime(new Date())
              .state(ProvisioningState.AVAILABLE)
              .build();

      Image actual1 = fnImage.apply(snapshot1);

      Image expected1 = new ImageBuilder()
              .ids(snapshot1.id())
              .name(snapshot1.name())
              .location(location)
              .status(Image.Status.AVAILABLE)
              .operatingSystem(OperatingSystem.builder()
                      .description(snapshot1.description())
                      .family(OsFamily.LINUX)
                      .is64Bit(true)
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "snapshot"))
              .build();

      assertEquals(actual1, expected1);

      Snapshot snapshot2 = Snapshot.builder()
              .isBootable(true)
              .isCpuHotPlug(true)
              .isCpuHotUnPlug(false)
              .isDiscVirtioHotPlug(true)
              .isDiscVirtioHotUnPlug(true)
              .id("d80bf9c0-ce6e-4283-9ea4-2906635f6137")
              .name("jclouds-ubuntu14.10-template")
              .description("Created from \"jclouds-ubuntu14.10 Storage\" in Data Center \"jclouds-computeservice\"")
              .size(10240f)
              .location(US_LAS)
              .isNicHotPlug(true)
              .isNicHotUnPlug(true)
              .osType(OsType.LINUX)
              .isRamHotPlug(true)
              .isRamHotUnPlug(false)
              .creationTime(date)
              .lastModificationTime(new Date())
              .state(ProvisioningState.INPROCESS)
              .build();

      Image actual2 = fnImage.apply(snapshot2);

      Image expected2 = new ImageBuilder()
              .ids(snapshot2.id())
              .name(snapshot2.name())
              .location(location)
              .status(Image.Status.PENDING)
              .operatingSystem(OperatingSystem.builder()
                      .description("ubuntu")
                      .family(OsFamily.UBUNTU)
                      .is64Bit(true)
                      .version("00.00")
                      .build())
              .userMetadata(ImmutableMap.of("provisionableType", "snapshot"))
              .build();

      assertEquals(actual2, expected2);
      assertEquals(actual2.getOperatingSystem(), expected2.getOperatingSystem());

   }
}
