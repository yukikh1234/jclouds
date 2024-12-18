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
package org.jclouds.util;

import static org.jclouds.util.Strings2.urlDecode;
import static org.jclouds.util.Strings2.urlEncode;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

@Test(groups = "unit")
public class Strings2Test {

   public void testReplaceTokens() {
      assertEquals(Strings2.replaceTokens("hello {where}", ImmutableMap.of("where", "world")), "hello world");
      assertEquals(Strings2.replaceTokens("hello {where}", ImmutableMap.of("where", "$1,000,000 \\o/!")), "hello $1,000,000 \\o/!");
      assertEquals(Strings2.replaceTokens("hello {where}", ImmutableMultimap.of("where", "$1,000,000 \\o/!")), "hello $1,000,000 \\o/!");
   }

   public void testUrlEncodeDecodeShouldGiveTheSameString() {
      String actual = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCc903twxU2zcQnIJdXv61RwZNZW94uId9qz08fgsBJsCOnHNIC4+L9k" +
         "DOA2IHV9cUfEDBm1Be5TbpadWwSbS/05E+FARH2/MCO932UgcKUq5PGymS0249fLCBPci5zoLiG5vIym+1ij1hL/nHvkK99NIwe7io+Lmp" +
         "9OcF3PTsm3Rgh5T09cRHGX9horp0VoAVa9vKJx6C1/IEHVnG8p0YPPa1lmemvx5kNBEiyoNQNYa34EiFkcJfP6rqNgvY8h/j4nE9SXoUCC" +
         "/g6frhMFMOL0tzYqvz0Lczqm1Oh4RnSn3O9X4R934p28qqAobe337hmlLUdb6H5zuf+NwCh0HdZ";
      assertEquals(actual, urlDecode(urlEncode(actual)));
   }

   public void testIsCidrFormat() {
      assert Strings2.isCidrFormat("1.2.3.4/5");
      assert Strings2.isCidrFormat("0.0.0.0/0");
      assert Strings2.isCidrFormat("fe80::/64");
      assert Strings2.isCidrFormat("fdcf:11a8:b89f::/64");
      assert Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af");

      for (int n = 0; n <= 128; n = n + 1) {
         assert Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/" + n);
      }

      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/129");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/b");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/*");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/@");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/00");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/01");
      assert !Strings2.isCidrFormat("fe80:fd6d:96a8:b89f:abcd:11aa:1234:15af/");

      assert !Strings2.isCidrFormat("banana");
      assert !Strings2.isCidrFormat("1.2.3.4");
      assert !Strings2.isCidrFormat("500.500.500.500/2423");
   }

}
