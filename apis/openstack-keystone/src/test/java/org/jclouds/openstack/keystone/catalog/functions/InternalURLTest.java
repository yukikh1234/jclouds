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
package org.jclouds.openstack.keystone.catalog.functions;

import static org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface.INTERNAL;
import static org.testng.Assert.assertEquals;

import java.net.URI;
import java.util.Collections;

import org.jclouds.openstack.keystone.catalog.ServiceEndpoint;
import org.testng.annotations.Test;

/**
 */
@Test(groups = "unit", testName = "InternalURLTest")
public class InternalURLTest {
   private final InternalURL fn = new InternalURL();

   public void testInternalURL() {
      assertEquals(
            fn.apply(Collections.singletonList(ServiceEndpoint.builder().type("cdn").regionId("regionOne")
                  .version("2.0")
                  .url(URI.create("https://ericsson.com/v2/1900e98b-7272-4cbd-8e95-0b8c2a9266c0"))
                  .iface(INTERNAL).build())).get(),
            URI.create("https://ericsson.com/v2/1900e98b-7272-4cbd-8e95-0b8c2a9266c0"));
   }
}
