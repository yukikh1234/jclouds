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
package org.jclouds.profitbricks.http.parser;

import org.jclouds.http.functions.ParseSax;
import org.xml.sax.SAXException;

public abstract class BaseProfitBricksResponseHandler<T> extends ParseSax.HandlerForGeneratedRequestWithResult<T> {

   private final StringBuilder strBuilder;

   public BaseProfitBricksResponseHandler() {
      this.strBuilder = new StringBuilder();
   }

   @Override
   public void characters(char[] ch, int start, int length) {
      strBuilder.append(ch, start, length);
   }

   protected String textToStringValue() {
      return strBuilder.toString().trim();
   }

   protected Float textToFloatValue() {
      return Float.valueOf(textToStringValue());
   }

   protected Double textToDoubleValue() {
      return Double.valueOf(textToStringValue());
   }

   protected int textToIntValue() {
      return Integer.parseInt(textToStringValue());
   }

   protected boolean textToBooleanValue() {
      return Boolean.parseBoolean(textToStringValue());
   }

   protected void clearTextBuffer() {
      strBuilder.setLength(0);
   }

   public void reset() {

   }

   @Override
   public abstract void endElement(String uri, String localName, String qName) throws SAXException;

   protected abstract void setPropertyOnEndTag(String qName);

}
