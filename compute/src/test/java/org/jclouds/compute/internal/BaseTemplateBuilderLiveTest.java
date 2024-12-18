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
package org.jclouds.compute.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.compute.util.ComputeServiceUtils.getCores;
import static org.jclouds.utils.TestUtils.NO_INVOCATIONS;
import static org.jclouds.utils.TestUtils.SINGLE_NO_ARG_INVOCATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.domain.internal.ArbitraryCpuRamTemplateBuilderImpl;
import org.jclouds.compute.util.AutomaticHardwareIdSpec;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.rest.config.CredentialStoreModule;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.inject.Module;

@Test(groups = "integration,live")
public abstract class BaseTemplateBuilderLiveTest extends BaseComputeServiceContextLiveTest {

   public void testCompareSizes() throws Exception {
      Hardware defaultSize = view.getComputeService().templateBuilder().build().getHardware();

      Hardware smallest = view.getComputeService().templateBuilder().smallest().build().getHardware();
      Hardware fastest = view.getComputeService().templateBuilder().fastest().build().getHardware();
      Hardware biggest = view.getComputeService().templateBuilder().biggest().build().getHardware();

      assertEquals(defaultSize, smallest);

      assert getCores(smallest) <= getCores(fastest) : String.format("%s ! <= %s", smallest, fastest);
      assert getCores(biggest) <= getCores(fastest) : String.format("%s ! <= %s", biggest, fastest);

      assert biggest.getRam() >= fastest.getRam() : String.format("%s ! >= %s", biggest, fastest);
      assert biggest.getRam() >= smallest.getRam() : String.format("%s ! >= %s", biggest, smallest);

      assert getCores(fastest) >= getCores(biggest) : String.format("%s ! >= %s", fastest, biggest);
      assert getCores(fastest) >= getCores(smallest) : String.format("%s ! >= %s", fastest, smallest);
   }

   public void testFromTemplate() {
      Template defaultTemplate = view.getComputeService().templateBuilder().build();
      assertEquals(view.getComputeService().templateBuilder().fromTemplate(defaultTemplate).build().toString(),
            defaultTemplate.toString());
   }

   @Test
   public void testTemplateBuilderCanUseImageId() throws Exception {
      Template defaultTemplate = view.getComputeService().templateBuilder().build();
      view.close();
      setupContext();

      Template template = view.getComputeService().templateBuilder().imageId(defaultTemplate.getImage().getId())
            .locationId(defaultTemplate.getLocation().getId()).build();
      assertEquals(template.getImage(), defaultTemplate.getImage());
   }

   @Test
   public void testDefaultTemplateBuilder() throws IOException {
      Template defaultTemplate = view.getComputeService().templateBuilder().build();
      assertTrue(defaultTemplate.getImage().getOperatingSystem().getVersion().matches("\\d+\\.\\d+"),
            "Version mismatch, expected dd.dd, found: " + defaultTemplate.getImage().getOperatingSystem().getVersion());
      assertEquals(defaultTemplate.getImage().getOperatingSystem().is64Bit(), true);
      assertEquals(defaultTemplate.getImage().getOperatingSystem().getFamily(), OsFamily.UBUNTU);
      assertEquals(getCores(defaultTemplate.getHardware()), 1.0d);
   }

   protected abstract Set<String> getIso3166Codes();

   @Test(groups = { "integration", "live" })
   public void testGetAssignableLocations() throws Exception {
      assertProvider(view.unwrap());
      for (Location location : view.getComputeService().listAssignableLocations()) {
         assert location.getId() != null : location;
         assert location != location.getParent() : location;
         assert location.getScope() != null : location;
         switch (location.getScope()) {
         case PROVIDER:
            assertProvider(location);
            break;
         case REGION:
            assertProvider(location.getParent());
            assert location.getIso3166Codes().size() == 0
                  || location.getParent().getIso3166Codes().containsAll(location.getIso3166Codes()) : location + " ||"
                  + location.getParent();
            break;
         case ZONE:
            Location provider = location.getParent().getParent();
            // zone can be a direct descendant of provider
            if (provider == null)
               provider = location.getParent();
            assertProvider(provider);
            assert location.getIso3166Codes().size() == 0
                  || location.getParent().getIso3166Codes().containsAll(location.getIso3166Codes()) : location + " ||"
                  + location.getParent();
            break;
         case SYSTEM:
            Location systemParent = location.getParent();
            // loop up to root, which must be the provider
            while (systemParent.getParent() != null) {
                systemParent = systemParent.getParent();
            }
            assertProvider(systemParent);
            break;
         case NETWORK:
             Location networkParent = location.getParent();
             // loop up to root, which must be the provider
             while (networkParent.getParent() != null) {
                 networkParent = networkParent.getParent();
             }
             assertProvider(networkParent);
             break;
         case HOST:
            Location provider2 = location.getParent().getParent().getParent();
            // zone can be a direct descendant of provider
            if (provider2 == null)
               provider2 = location.getParent().getParent();
            assertProvider(provider2);
            break;
         }
      }
   }

   @Test
   public void testTemplateBuilderWithImageIdSpecified() throws IOException {
      Template defaultTemplate = view.getComputeService().templateBuilder().build();

      ComputeServiceContext context = null;
      try {
         Properties overrides = setupProperties();
         overrides.setProperty("jclouds.image-id", defaultTemplate.getImage().getId());

         context = createView(overrides, setupModules());

         assertEqualsTemplate(context.getComputeService().templateBuilder().build(), defaultTemplate);
      } finally {
         if (context != null)
            context.close();
      }

      context = null;
      try {
         Properties overrides = setupProperties();
         overrides.setProperty(provider + ".image-id", defaultTemplate.getImage().getId());

         context = createView(overrides, setupModules());

         assertEqualsTemplate(context.getComputeService().templateBuilder().build(), defaultTemplate);
      } finally {
         if (context != null)
            context.close();
      }
   }

   @Test
   public void testTemplateBuilderWithLoginUserSpecified() throws IOException {
      tryOverrideUsingPropertyKey("jclouds");
      tryOverrideUsingPropertyKey(provider);
   }

   protected void tryOverrideUsingPropertyKey(String propertyKey) {
      // isolate tests from eachother, as default credentialStore is static
      Module credentialStoreModule = new CredentialStoreModule(
            new ConcurrentHashMap<String, ByteSource>());

      ComputeServiceContext context = null;
      try {
         Properties overrides = setupProperties();
         String login = templateBuilderSpec != null && templateBuilderSpec.getLoginUser() != null ? templateBuilderSpec
               .getLoginUser() : "foo:bar";
         overrides.setProperty(propertyKey + ".image.login-user", login);
         boolean auth = templateBuilderSpec != null && templateBuilderSpec.getAuthenticateSudo() != null ? templateBuilderSpec
               .getAuthenticateSudo() : true;
         overrides.setProperty(propertyKey + ".image.authenticate-sudo", auth + "");

         context = createView(overrides, ImmutableSet.<Module>of(credentialStoreModule));

         Iterable<String> userPass = Splitter.on(':').split(login);
         String user = Iterables.get(userPass, 0);
         String pass = Iterables.size(userPass) > 1 ? Iterables.get(userPass, 1) : null;
         assertEquals(context.getComputeService().templateBuilder().build().getImage().getDefaultCredentials(),
               LoginCredentials.builder().user(user).password(pass).authenticateSudo(auth).build());
      } finally {
         if (context != null) {
            context.close();
         }
      }
   }

   void assertProvider(Location provider) {
      assertEquals(provider.getScope(), LocationScope.PROVIDER);
      assertEquals(provider.getParent(), null);
      assertEquals(provider.getIso3166Codes(), getIso3166Codes());
   }

   static void assertEqualsTemplate(Template actual, Template expected){
      assertEquals(actual.getImage(), expected.getImage());
      assertEquals(actual.getHardware(), expected.getHardware());
      assertEquals(actual.getOptions(), expected.getOptions());
      assertTrue(actual.getLocation().getScope().compareTo(expected.getLocation().getScope()) <= 0);
   }

   @DataProvider
   public Object[][] onlyIfAutomaticHardwareSupported() {
      return  view.getComputeService().templateBuilder() instanceof ArbitraryCpuRamTemplateBuilderImpl ?
            SINGLE_NO_ARG_INVOCATION : NO_INVOCATIONS;
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"})
   public void testAutoGeneratedHardwareFromId() {
      Template template = view.getComputeService().templateBuilder()
            .hardwareId("automatic:cores=2;ram=1024").build();
      assertThat(template.getHardware().getId()).isEqualTo("automatic:cores=2.0;ram=1024");
      assertThat(template.getHardware().getRam()).isEqualTo(1024);
      assertThat(template.getHardware().getProcessors().get(0).getCores()).isEqualTo(2);
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"})
   public void testAutoGeneratedHardwareMatchHardwareProfile() {
      if (!view.getComputeService().listHardwareProfiles().isEmpty()) {
         Template template = view.getComputeService().templateBuilder()
               .minRam(2048).minCores(2).build();
         assertThat(AutomaticHardwareIdSpec.isAutomaticId(template.getHardware().getId())).isFalse();
         assertThat(template.getHardware().getRam()).isGreaterThanOrEqualTo(2048);
         assertThat(template.getHardware().getProcessors().get(0).getCores()).isGreaterThanOrEqualTo(2);
      }
      else {
         throw new SkipException("Hardware profile list is empty, this provider can not match any hardware profile" +
               "to the specified minRam and minCores.");
      }
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"})
   public void testAutoGeneratedHardwareWithMinCoresAndMinRam() {
      if (view.getComputeService().listHardwareProfiles().isEmpty()) {
         Template template = view.getComputeService().templateBuilder()
               .minRam(2048).minCores(2).build();
         assertThat(AutomaticHardwareIdSpec.isAutomaticId(template.getHardware().getId())).isTrue();
         assertThat(template.getHardware().getRam()).isEqualTo(2048);
         assertThat(template.getHardware().getProcessors().get(0).getCores()).isEqualTo(2);
      }
      else {
         throw new SkipException("Hardware profile list not empty.");
      }
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"})
   public void testAutoGeneratedHardwareWithOnlyMinCoresMatchHardwareProfile() {
      if (!view.getComputeService().listHardwareProfiles().isEmpty()) {
         Template template = view.getComputeService().templateBuilder().minCores(4).build();
         assertThat(AutomaticHardwareIdSpec.isAutomaticId(template.getHardware().getId())).isFalse();
         assertThat(template.getHardware().getProcessors().get(0).getCores()).isGreaterThanOrEqualTo(4);
      }
      else {
         throw new SkipException("Hardware profile list is empty, this provider can not match any hardware profile" +
               "to the specified minRam and minCores.");
      }
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"})
   public void testAutoGeneratedHardwareWithOnlyMinRamMatchHardwareProfile() {
      if (!view.getComputeService().listHardwareProfiles().isEmpty()) {
         Template template = view.getComputeService().templateBuilder().minRam(4096).build();
         assertThat(AutomaticHardwareIdSpec.isAutomaticId(template.getHardware().getId())).isFalse();
         assertThat(template.getHardware().getRam()).isGreaterThanOrEqualTo(4096);
      }
      else {
         throw new SkipException("Hardware profile list is empty, this provider can not match any hardware profile" +
               "to the specified minRam and minCores.");
      }
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"},
         expectedExceptions = IllegalArgumentException.class,
         expectedExceptionsMessageRegExp = "No hardware profile matching the given criteria was found. " +
         "If you want to use exact values, please set the minCores and minRam values")
   public void testAutoGeneratedHardwareWithOnlyMinRamNotMatchHardwareProfile() {
      if (view.getComputeService().listHardwareProfiles().isEmpty()) {
         view.getComputeService().templateBuilder().minRam(4096).build();
      }
      else {
         throw new SkipException("Hardware profile list not empty.");
      }
   }

   @Test(dataProvider = "onlyIfAutomaticHardwareSupported", groups = {"integration", "live"},
         expectedExceptions = IllegalArgumentException.class,
         expectedExceptionsMessageRegExp = "No hardware profile matching the given criteria was found. " +
               "If you want to use exact values, please set the minCores and minRam values")
   public void testAutoGeneratedHardwareWithOnlyMinCoresNotMatchHardwareProfile() {
      if (view.getComputeService().listHardwareProfiles().isEmpty()) {
         view.getComputeService().templateBuilder().minCores(4).build();
      }
      else {
         throw new SkipException("Hardware profile list not empty.");
      }
   }

}
