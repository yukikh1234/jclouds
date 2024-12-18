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
package org.jclouds.openstack.keystone.config;

import org.jclouds.openstack.keystone.auth.config.CredentialTypes;
import org.jclouds.rest.annotations.SinceApiVersion;

/**
 * Configuration properties and constants used in Keystone connections.
 */
public final class KeystoneProperties {

   /**
    * Type of credentials used to log into the auth service.
    *
    * <h3>valid values</h3>
    * <ul>
    * <li>apiAccessKeyCredentials</li>
    * <li>passwordCredentials</li>
    * <li>tokenCredentials</li>
    * </ul>
    *
    * @see CredentialTypes
    * @see <a href=
    *      "http://docs.openstack.org/api/openstack-identity-service/2.0/content/POST_authenticate_v2.0_tokens_Service_API_Api_Operations.html"
    *      />
    */
   public static final String CREDENTIAL_TYPE = "jclouds.keystone.credential-type";

   /**
    * set this property to specify the tenant id of the authenticated user.
    * Cannot be used simultaneously with {@link #TENANT_NAME}
    *
    * @see <a href="http://wiki.openstack.org/CLIAuth">openstack docs</a>
    */
   public static final String TENANT_ID = "jclouds.keystone.tenant-id";

   /**
    * set this property to specify the tenant name of the authenticated user.
    * Cannot be used simultaneously with {@link #TENANT_ID}
    *
    * @see <a href="http://wiki.openstack.org/CLIAuth">openstack docs</a>
    */
   public static final String TENANT_NAME = "jclouds.keystone.tenant-name";

   /**
    * set this property to {@code true} to designate that the service requires
    * explicit specification of either {@link #TENANT_NAME} or
    * {@link #TENANT_ID}
    *
    * @see <a href="http://wiki.openstack.org/CLIAuth">openstack docs</a>
    */
   public static final String REQUIRES_TENANT = "jclouds.keystone.requires-tenant";

   /**
    * set this property to specify for scoped authentication.
    * <p>
    * The format is one of the following:
    * <ul>
    * <li>project:<project-id></li>
    * <li>domain:<domain-name></li>
    * <li></li>
    * </ul>
    * For example: <code>project:457841231597451534</code>
    */
   @SinceApiVersion("3")
   public static final String SCOPE = "jclouds.keystone.scope";

   /**
    * Set this property to specify the domain name of project (tenant)
    * scope.<br/>
    * Required property when authentication {@link #SCOPE} is 'project:' and
    * project (tenant) domain is different than the user domain (Otherwise, the
    * domain used is the same as the user). <br/>
    * Cannot be used simultaneously with {@link #PROJECT_DOMAIN_ID}
    *
    * @see <a href=
    *      "https://docs.openstack.org/keystone/latest/api_curl_examples.html#project-scoped">openstack
    *      docs : Identity service (Keystone)</a>
    */
   public static final String PROJECT_DOMAIN_NAME = "jclouds.keystone.project-domain-name";

   /**
    * Set this property to specify the domain id of project (tenant) scope.<br/>
    * Required property when authentication {@link #SCOPE} is 'project:' and
    * project (tenant) domain is different than the user domain (Otherwise, the
    * domain used is the same as the user). <br/>
    * Cannot be used simultaneously with {@link #PROJECT_DOMAIN_NAME}
    *
    * 
    * @see <a href=
    *      "https://docs.openstack.org/keystone/latest/api_curl_examples.html#project-scoped">openstack
    *      docs : Identity service (Keystone)</a>
    */
   public static final String PROJECT_DOMAIN_ID = "jclouds.keystone.project-domain-id";

   /**
    * type of the keystone service. ex. {@code compute}
    *
    * @see ServiceType
    */
   public static final String SERVICE_TYPE = "jclouds.keystone.service-type";

   /**
    * Version of keystone to be used by services. Default: 3.
    */
   public static final String KEYSTONE_VERSION = "jclouds.keystone.version";

   private KeystoneProperties() {
      throw new AssertionError("intentionally unimplemented");
   }
}
