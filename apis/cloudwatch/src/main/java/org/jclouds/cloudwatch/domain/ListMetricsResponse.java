
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
package org.jclouds.cloudwatch.domain;

import java.util.Set;

import org.jclouds.javax.annotation.Nullable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableSet;

/**
 * list of {@link Metric}
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_ListMetrics.html" />
 */
public class ListMetricsResponse extends ForwardingSet<Metric> {

   private final Set<Metric> metrics;
   private final String nextToken;

   public ListMetricsResponse(@Nullable Set<Metric> metrics, @Nullable String nextToken) {
      this.metrics = metrics == null ? ImmutableSet.<Metric>of() : ImmutableSet.copyOf(metrics);
      this.nextToken = nextToken;
   }

   /**
    * return the next token or null if there is none.
    */
   @Nullable
   public String getNextToken() {
      return nextToken;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      return Objects.hashCode(metrics, nextToken);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof ListMetricsResponse)) return false;
      ListMetricsResponse other = (ListMetricsResponse) obj;
      return Objects.equal(metrics, other.metrics) && Objects.equal(nextToken, other.nextToken);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
                    .add("metrics", metrics)
                    .add("nextToken", nextToken).toString();
   }

   @Override
   protected Set<Metric> delegate() {
      return metrics;
   }

}
