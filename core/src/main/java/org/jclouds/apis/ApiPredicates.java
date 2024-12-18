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
package org.jclouds.apis;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;

import org.jclouds.View;
import org.jclouds.util.TypeTokenUtils;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.reflect.TypeToken;

/**
 * Container for api filters (predicates).
 */
public class ApiPredicates {

   /**
    * Returns all apis available to jclouds regardless of type.
    * 
    * @return all available apis
    */
   public static Predicate<ApiMetadata> all() {
      return Predicates.<ApiMetadata> alwaysTrue();
   }

   /**
    * Returns all apis with the given id.
    * 
    * @param id
    *           the id of the api to return
    * 
    * @return the apis with the given id
    */
   public static Predicate<ApiMetadata> id(final String id) {
      checkNotNull(emptyToNull(id), "id must be defined");
      return new Predicate<ApiMetadata>() {
         /**
          * {@inheritDoc}
          */
         @Override
         public boolean apply(ApiMetadata apiMetadata) {
            return apiMetadata.getId().equals(id);
         }

         /**
          * {@inheritDoc}
          */
         @Override
         public String toString() {
            return "id(" + id + ")";
         }
      };
   }

   /**
    * Returns all apis who's contexts are assignable from the parameter
    * 
    * @param type
    *           the type of the context to search for
    * 
    * @return the apis with contexts assignable from given type
    */
   public static Predicate<ApiMetadata> contextAssignableFrom(final TypeToken<?> type) {
      checkNotNull(type, "context must be defined");
      return new Predicate<ApiMetadata>() {
         /**
          * {@inheritDoc}
          */
         @Override
         public boolean apply(ApiMetadata apiMetadata) {
            return TypeTokenUtils.isSupertypeOf(type, apiMetadata.getContext());
         }

         /**
          * {@inheritDoc}
          */
         @Override
         public String toString() {
            return "contextAssignableFrom(" + type + ")";
         }
      };
   }

   /**
    * Returns all apis who's contexts are transformable to the parameter
    * 
    * @param type
    *           the type of the context to search for
    * 
    * @return the apis with contexts transformable to the given type
    */
   public static Predicate<ApiMetadata> viewableAs(final TypeToken<?> type) {
      checkNotNull(type, "context must be defined");
      return new Predicate<ApiMetadata>() {
         /**
          * {@inheritDoc}
          */
         @Override
         public boolean apply(ApiMetadata apiMetadata) {
            for (TypeToken<? extends View> to : apiMetadata.getViews())
               if (TypeTokenUtils.isSupertypeOf(type, to))
                  return true;
            return false;
         }

         /**
          * {@inheritDoc}
          */
         @Override
         public String toString() {
            return "viewableAs(" + type + ")";
         }
      };
   }
}
