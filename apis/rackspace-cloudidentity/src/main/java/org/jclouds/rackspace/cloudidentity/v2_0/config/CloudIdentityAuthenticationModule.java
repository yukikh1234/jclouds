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
package org.jclouds.rackspace.cloudidentity.v2_0.config;

import static org.jclouds.rest.config.BinderUtils.bindHttpApi;

import java.util.Map;

import org.jclouds.domain.Credentials;
import org.jclouds.openstack.keystone.auth.AuthenticationApi;
import org.jclouds.openstack.keystone.auth.config.AuthenticationModule;
import org.jclouds.openstack.keystone.auth.config.CredentialTypes;
import org.jclouds.openstack.keystone.auth.domain.AuthInfo;
import org.jclouds.openstack.keystone.auth.functions.AuthenticatePasswordCredentials;
import org.jclouds.rackspace.cloudidentity.v2_0.CloudIdentityAuthenticationApi;
import org.jclouds.rackspace.cloudidentity.v2_0.functions.AuthenticateApiKeyCredentials;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;
import com.google.inject.Injector;

public class CloudIdentityAuthenticationModule extends AuthenticationModule {

   @Override
   protected void configure() {
      super.configure();
      bindHttpApi(binder(), CloudIdentityAuthenticationApi.class);
   }

   @Override
   protected Map<String, AuthenticationApi> authenticationApis(Injector i) {
      Map<String, AuthenticationApi> authenticationApis = Maps.newHashMap();
      authenticationApis.put("2", i.getInstance(CloudIdentityAuthenticationApi.class));
      authenticationApis.put("3", i.getInstance(CloudIdentityAuthenticationApi.class));
      return authenticationApis;
   }

   @Override
   protected Map<String, Function<Credentials, AuthInfo>> authenticationMethods(Injector i) {
      Builder<Function<Credentials, AuthInfo>> fns = ImmutableSet.<Function<Credentials, AuthInfo>> builder();
      fns.add(i.getInstance(AuthenticatePasswordCredentials.class));
      fns.add(i.getInstance(AuthenticateApiKeyCredentials.class));
      return CredentialTypes.indexByCredentialType(fns.build());
   }

}
