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
package org.jclouds.rackspace.clouddatabases.uk;

import static org.jclouds.location.reference.LocationConstants.ISO3166_CODES;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGION;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGIONS;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.CREDENTIAL_TYPE;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.KEYSTONE_VERSION;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.SERVICE_TYPE;

import java.net.URI;
import java.util.Properties;

import org.jclouds.openstack.keystone.catalog.config.ServiceCatalogModule;
import org.jclouds.openstack.keystone.catalog.config.ServiceCatalogModule.RegionModule;
import org.jclouds.openstack.trove.v1.TroveApiMetadata;
import org.jclouds.openstack.trove.v1.config.TroveHttpApiModule;
import org.jclouds.openstack.trove.v1.config.TroveParserModule;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.internal.BaseProviderMetadata;
import org.jclouds.rackspace.cloudidentity.v2_0.ServiceType;
import org.jclouds.rackspace.cloudidentity.v2_0.config.CloudIdentityAuthenticationModule;
import org.jclouds.rackspace.cloudidentity.v2_0.config.CloudIdentityCredentialTypes;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ProviderMetadata} for Rackspace Cloud Databases.
 */
@AutoService(ProviderMetadata.class)
public class CloudDatabasesUKProviderMetadata extends BaseProviderMetadata {

   public static Builder builder() {
      return new Builder();
   }

   @Override
   public Builder toBuilder() {
      return builder().fromProviderMetadata(this);
   }

   public CloudDatabasesUKProviderMetadata() {
      super(builder());
   }

   public CloudDatabasesUKProviderMetadata(Builder builder) {
      super(builder);
   }

   public static Properties defaultProperties() {
      Properties properties = new Properties();
      properties.setProperty(CREDENTIAL_TYPE, CloudIdentityCredentialTypes.API_KEY_CREDENTIALS);
      properties.setProperty(SERVICE_TYPE, ServiceType.DATABASES);
      properties.setProperty(KEYSTONE_VERSION, "2");
      properties.setProperty(PROPERTY_REGIONS, "LON");
      properties.setProperty(PROPERTY_REGION + ".LON." + ISO3166_CODES, "GB-SLG");
      return properties;
   }

   public static class Builder extends BaseProviderMetadata.Builder {

      protected Builder(){
         id("rackspace-clouddatabases-uk")
         .name("Rackspace Clouddatabases UK")
         .apiMetadata(new TroveApiMetadata().toBuilder()
                  .identityName("${userName}")
                  .credentialName("${apiKey}")
                  .defaultEndpoint("https://lon.identity.api.rackspacecloud.com/v2.0")
                  .endpointName("identity service url ending in /v2.0/")
                  .documentation(URI.create("http://docs.rackspace.com/cbs/api/v1.0/cbs-devguide/content/overview.html"))
                  .defaultModules(ImmutableSet.<Class<? extends Module>>builder()
                                              .add(CloudIdentityAuthenticationModule.class)
                                              .add(ServiceCatalogModule.class)
                                              .add(RegionModule.class)
                                              .add(TroveParserModule.class)
                                              .add(TroveHttpApiModule.class).build())
                  .build())
         .homepage(URI.create("http://www.rackspace.com/cloud/public/databases/"))
         .console(URI.create("https://mycloud.rackspace.com"))
         .linkedServices("rackspace-cloudservers-uk", "cloudfiles-uk")
         .iso3166Codes("GB-SLG")
         .endpoint("https://lon.identity.api.rackspacecloud.com/v2.0")
         .defaultProperties(CloudDatabasesUKProviderMetadata.defaultProperties());
      }

      @Override
      public CloudDatabasesUKProviderMetadata build() {
         return new CloudDatabasesUKProviderMetadata(this);
      }

      @Override
      public Builder fromProviderMetadata(ProviderMetadata in) {
         super.fromProviderMetadata(in);
         return this;
      }
   }

}
