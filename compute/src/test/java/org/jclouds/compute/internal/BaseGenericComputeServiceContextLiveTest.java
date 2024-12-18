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

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.jclouds.apis.BaseViewLiveTest;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.domain.TemplateBuilderSpec;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.domain.LoginCredentials.Builder;
import org.jclouds.rest.config.CredentialStoreModule;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteSource;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public abstract class BaseGenericComputeServiceContextLiveTest<W extends ComputeServiceContext> extends BaseViewLiveTest<W> {

   protected TemplateBuilderSpec templateBuilderSpec;
   protected LoginCredentials loginCredentials = LoginCredentials.builder().user("root").build();

   // isolate tests from eachother, as default credentialStore is static
   protected Module credentialStoreModule = new CredentialStoreModule(
         new ConcurrentHashMap<String, ByteSource>());
   
   @Override
   protected Properties setupProperties() {
      Properties overrides = super.setupProperties();
      String spec = setIfTestSystemPropertyPresent(overrides, provider + ".template");
      if (spec != null) {
         templateBuilderSpec = TemplateBuilderSpec.parse(spec);
         if (templateBuilderSpec.getLoginUser() != null) {
            Iterable<String> userPass = Splitter.on(':').split(templateBuilderSpec.getLoginUser());
            Builder loginCredentialsBuilder = LoginCredentials.builder();
            loginCredentialsBuilder.user(Iterables.get(userPass, 0));
            if (Iterables.size(userPass) == 2)
               loginCredentialsBuilder.password(Iterables.get(userPass, 1));
            if (templateBuilderSpec.getAuthenticateSudo() != null)
               loginCredentialsBuilder.authenticateSudo(templateBuilderSpec.getAuthenticateSudo());
            loginCredentials = loginCredentialsBuilder.build();
         }
      }
      return overrides;
   }

   @Override
   protected Iterable<Module> setupModules() {
      return ImmutableSet.<Module> of(getLoggingModule(), credentialStoreModule, getSshModule());
   }
   
   protected Module getSshModule() {
      return Modules.EMPTY_MODULE;
   }
}
