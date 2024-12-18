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
package org.jclouds.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jclouds.http.Uris.uriBuilder;
import static org.jclouds.util.Strings2.urlEncode;
import static org.testng.Assert.assertEquals;

import java.net.URI;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableMap;

@Test
public class UrisTest {

   private static final ImmutableMap<String, String> templateParams = ImmutableMap.of("user", "bob");

   public void testScheme() {
      assertEquals(uriBuilder("https://foo.com:8080").scheme("http").toString(), "http://foo.com:8080");
      assertEquals(uriBuilder("https://foo.com:8080").scheme("http").build().toString(), "http://foo.com:8080");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").scheme("http").toString(),
            "http://api.github.com/repos/user?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").scheme("http").build().toString(),
            "http://api.github.com/repos/user?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").scheme("http").toString(),
            "http://api.github.com/repos/{user}");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").scheme("http").build(templateParams)
            .toASCIIString(), "http://api.github.com/repos/bob");

   }

   public void testHost() {
      assertEquals(uriBuilder("https://foo.com:8080").host("robots").toString(), "https://robots:8080");
      assertEquals(uriBuilder("https://foo.com:8080").host("robots").build().toString(), "https://robots:8080");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").host("robots").toString(),
            "https://robots/repos/user?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").host("robots").build().toString(),
            "https://robots/repos/user?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").host("robots").toString(),
            "https://robots/repos/{user}");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").host("robots").build(templateParams)
            .toASCIIString(), "https://robots/repos/bob");
   }

   @DataProvider(name = "strings")
   public Object[][] createData() {
      return new Object[][] { { "normal" }, { "sp ace" }, { "qu?stion" }, { "unic₪de" }, { "path/foo" }, { "colon:" },
            { "asteri*k" }, { "quote\"" }, { "great<r" }, { "lesst>en" }, { "p|pe" } };
   }

   @Test(dataProvider = "strings")
   public void testQuery(String val) {
      assertThat(uriBuilder("https://foo.com:8080").addQuery("moo", val).build().getQuery())
            .isEqualTo("moo=" + val);
      assertThat(uriBuilder("https://foo.com:8080").addQuery("moo", val).build().getRawQuery())
            .isEqualTo("moo=" + urlEncode(val, '/', ','));
      assertThat(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").addQuery("moo", val).build()
            .getQuery())
            .isEqualTo("foo=bar&kung=fu&moo=" + val);
      assertThat(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").addQuery("moo", val).build()
            .getRawQuery())
            .isEqualTo("foo=bar&kung=fu&moo=" + urlEncode(val, '/', ','));
      assertThat(uriBuilder("https://api.github.com/repos/{user}").addQuery("moo", val).build().getQuery())
            .isEqualTo("moo=" + val);
      assertThat(uriBuilder("https://api.github.com/repos/{user}").addQuery("moo", val).toString())
            .isEqualTo("https://api.github.com/repos/{user}?moo=" + urlEncode(val, '/', ','));
      assertThat(uriBuilder("https://api.github.com/repos/{user}").addQuery("moo", val).build(templateParams)
            .getRawQuery())
            .isEqualTo("moo=" + urlEncode(val, '/', ','));
      assertThat(uriBuilder("https://api.github.com/repos/{user}").addQuery("moo", val).build(templateParams)
            .getPath())
            .isEqualTo("/repos/bob");
   }

   @Test(dataProvider = "strings")
   public void testPath(String path) {
      assertEquals(uriBuilder("https://foo.com:8080").path(path).toString(), "https://foo.com:8080/" + path);
      assertEquals(uriBuilder("https://foo.com:8080").path(path).build().toString(), "https://foo.com:8080/"
            + urlEncode(path, '/', ':', ';', '='));
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").path(path).toString(),
            "https://api.github.com/" + path + "?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").path(path).build().toString(),
            "https://api.github.com/" + urlEncode(path, '/', ':', ';', '=') + "?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").path(path).toString(), "https://api.github.com/"
            + path);
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").path(path).build(templateParams).toASCIIString(),
            "https://api.github.com/" + urlEncode(path, '/', ':', ';', '='));
   }

   @Test(dataProvider = "strings")
   public void testAppendPath(String path) {
      assertEquals(uriBuilder("https://foo.com:8080").appendPath(path).toString(), "https://foo.com:8080/" + path);
      assertEquals(uriBuilder("https://foo.com:8080").appendPath(path).build().toString(), "https://foo.com:8080/"
            + urlEncode(path, '/', ':', ';', '='));
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").appendPath(path).toString(),
            "https://api.github.com/repos/user/" + path + "?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/user?foo=bar&kung=fu").appendPath(path).build().toString(),
            "https://api.github.com/repos/user/" + urlEncode(path, '/', ':', ';', '=') + "?foo=bar&kung=fu");
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").appendPath(path).toString(),
            "https://api.github.com/repos/{user}/" + path);
      assertEquals(uriBuilder("https://api.github.com/repos/{user}").appendPath(path).build(templateParams)
            .toASCIIString(), "https://api.github.com/repos/bob/" + urlEncode(path, '/', ':', ';', '='));
   }

   @Test
   public void testNoDoubleSlashInPath() {
      assertEquals(uriBuilder("https://cloud/api/").appendPath("/").build().toASCIIString(), "https://cloud/api/");
   }

   @Test
   public void testWhenMatrixOnPath() {
      assertEquals(
            uriBuilder("https://api.rimuhosting.com/r").appendPath("orders;include_inactive=N").build(templateParams)
                  .toASCIIString(), "https://api.rimuhosting.com/r/orders;include_inactive=N");
   }

   @Test(dataProvider = "strings")
   public void testReplaceQueryIsEncoded(String key) {
      assertThat(uriBuilder("/redirect").addQuery("foo", key).build().getQuery()).isEqualTo("foo=" + key);
      assertThat(uriBuilder("/redirect").addQuery("foo", key).build().getRawQuery())
            .isEqualTo("foo=" + urlEncode(key, '/', ','));
   }

   public void testAddQuery() {
      assertEquals(uriBuilder("http://localhost:8080/client/api").addQuery("response", "json").toString(),
            "http://localhost:8080/client/api?response=json");

      assertEquals(
            uriBuilder(URI.create("http://localhost:8080/client/api")).addQuery("response", "json")
                  .addQuery("command", "queryAsyncJobResult").build().toString(),
            "http://localhost:8080/client/api?response=json&command=queryAsyncJobResult");
   }
}
