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
package org.jclouds.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

import com.google.common.io.ByteSource;

/**
 * Utility class for test
 */
public class TestUtils {

   public static final Object[][] NO_INVOCATIONS = new Object[0][0];
   public static final Object[][] SINGLE_NO_ARG_INVOCATION = { new Object[0] };

   public static boolean isJava6() {
      return System.getProperty("java.version", "").contains("1.6.");
   }

   public static ByteSource randomByteSource() {
      return randomByteSource(0);
   }

   public static ByteSource randomByteSource(long seed) {
      return new RandomByteSource(seed);
   }

   private static class RandomByteSource extends ByteSource {
      private final long seed;

      RandomByteSource(long seed) {
         this.seed = seed;
      }

      @Override
      public InputStream openStream() {
         return new RandomInputStream(seed);
      }
   }

   private static class RandomInputStream extends InputStream {
      private final Random random;
      private boolean closed = false;

      RandomInputStream(long seed) {
         this.random = new Random(seed);
      }

      @Override
      public synchronized int read() throws IOException {
         if (closed) {
            throw new IOException("Stream already closed");
         }
         // return value between 0 and 255
         return random.nextInt() & 0xff;
      }

      @Override
      public synchronized int read(byte[] b) throws IOException {
         return read(b, 0, b.length);
      }

      @Override
      public synchronized int read(byte[] b, int off, int len) throws IOException {
         for (int i = 0; i < len; ++i) {
            b[off + i] = (byte) read();
         }
         return len;
      }

      @Override
      public void close() throws IOException {
         super.close();
         closed = true;
      }
   }

   public static byte[] gzip(byte[] data) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
      GZIPOutputStream gos = new GZIPOutputStream(baos);
      gos.write(data, 0, data.length);
      gos.finish();
      return baos.toByteArray();
   }
}
