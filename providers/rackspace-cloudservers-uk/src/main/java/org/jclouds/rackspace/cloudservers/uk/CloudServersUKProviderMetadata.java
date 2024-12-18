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
package org.jclouds.rackspace.cloudservers.uk;

import static org.jclouds.compute.config.ComputeServiceProperties.TEMPLATE;
import static org.jclouds.location.reference.LocationConstants.ISO3166_CODES;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGION;
import static org.jclouds.location.reference.LocationConstants.PROPERTY_REGIONS;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.CREDENTIAL_TYPE;
import static org.jclouds.openstack.keystone.config.KeystoneProperties.KEYSTONE_VERSION;

import java.net.URI;
import java.util.Properties;

import org.jclouds.openstack.keystone.catalog.config.ServiceCatalogModule;
import org.jclouds.openstack.keystone.catalog.config.ServiceCatalogModule.RegionModule;
import org.jclouds.openstack.nova.v2_0.NovaApiMetadata;
import org.jclouds.openstack.nova.v2_0.config.NovaParserModule;
import org.jclouds.providers.ProviderMetadata;
import org.jclouds.providers.internal.BaseProviderMetadata;
import org.jclouds.rackspace.cloudidentity.v2_0.config.CloudIdentityAuthenticationModule;
import org.jclouds.rackspace.cloudidentity.v2_0.config.CloudIdentityCredentialTypes;
import org.jclouds.rackspace.cloudservers.uk.config.CloudServersUKComputeServiceContextModule;
import org.jclouds.rackspace.cloudservers.uk.config.CloudServersUKHttpApiModule;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ProviderMetadata} for Rackspace Next Generation Cloud Servers.
 */
@AutoService(ProviderMetadata.class)
public class CloudServersUKProviderMetadata extends BaseProviderMetadata
{

    public static Builder builder()
    {
        return new Builder();
    }

    @Override
    public Builder toBuilder()
    {
        return builder().fromProviderMetadata(this);
    }

    public CloudServersUKProviderMetadata()
    {
        super(builder());
    }

    public CloudServersUKProviderMetadata(final Builder builder)
    {
        super(builder);
    }

    public static Properties defaultProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(CREDENTIAL_TYPE, CloudIdentityCredentialTypes.API_KEY_CREDENTIALS);
        properties.setProperty(KEYSTONE_VERSION, "2");
        properties.setProperty(PROPERTY_REGIONS, "LON");
        properties.setProperty(PROPERTY_REGION + ".LON." + ISO3166_CODES, "GB-SLG");
        properties.setProperty(TEMPLATE, "imageNameMatches=.*Ubuntu.*");
        return properties;
    }

    public static class Builder extends BaseProviderMetadata.Builder
    {

        protected Builder()
        {
            id("rackspace-cloudservers-uk")
                .name("Rackspace Next Generation Cloud Servers UK")
                .apiMetadata(
                    new NovaApiMetadata()
                        .toBuilder()
                        .identityName("${userName}")
                        .credentialName("${apiKey}")
                        .version("2")
                        .defaultEndpoint("https://lon.identity.api.rackspacecloud.com/v2.0/")
                        .endpointName("identity service url ending in /v2.0/")
                        .documentation(
                            URI.create("http://docs.rackspace.com/servers/api/v2/cs-devguide/content/ch_preface.html#webhelp-currentid"))
                        .defaultModules(
                            ImmutableSet.<Class< ? extends Module>>builder()
                                .add(CloudIdentityAuthenticationModule.class)
                                .add(ServiceCatalogModule.class).add(RegionModule.class)
                                .add(NovaParserModule.class).add(CloudServersUKHttpApiModule.class)
                                .add(CloudServersUKComputeServiceContextModule.class).build())
                        .build()).homepage(URI.create("http://www.rackspace.co.uk/opencloud"))
                .console(URI.create("https://mycloud.rackspace.co.uk/"))
                .linkedServices("rackspace-cloudservers-uk", "cloudfiles-swift-uk")
                .iso3166Codes("GB-SLG")
                .endpoint("https://lon.identity.api.rackspacecloud.com/v2.0/")
                .defaultProperties(CloudServersUKProviderMetadata.defaultProperties());
        }

        @Override
        public CloudServersUKProviderMetadata build()
        {
            return new CloudServersUKProviderMetadata(this);
        }

        @Override
        public Builder fromProviderMetadata(final ProviderMetadata in)
        {
            super.fromProviderMetadata(in);
            return this;
        }
    }
}
