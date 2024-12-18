
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
package org.jclouds.byon.internal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.in;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.getOnlyElement;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Maps.filterKeys;

import java.util.Set;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.byon.Node;
import org.jclouds.byon.functions.NodeToNodeMetadata;
import org.jclouds.compute.JCloudsNativeComputeServiceAdapter;
import org.jclouds.compute.domain.Hardware;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationBuilder;
import org.jclouds.domain.LocationScope;
import org.jclouds.location.suppliers.all.JustProvider;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.util.concurrent.UncheckedExecutionException;

@Singleton
public class BYONComputeServiceAdapter implements JCloudsNativeComputeServiceAdapter {
   private final Supplier<LoadingCache<String, Node>> nodes;
   private final NodeToNodeMetadata converter;
   private final JustProvider locationSupplier;

   @Inject
   public BYONComputeServiceAdapter(Supplier<LoadingCache<String, Node>> nodes, NodeToNodeMetadata converter,
                                    JustProvider locationSupplier) {
      this.nodes = checkNotNull(nodes, "nodes");
      this.converter = checkNotNull(converter, "converter");
      this.locationSupplier = checkNotNull(locationSupplier, "locationSupplier");
   }

   @Override
   public NodeWithInitialCredentials createNodeWithGroupEncodedIntoName(String group, String name, Template template) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }

   @Override
   public Iterable<Hardware> listHardwareProfiles() {
      return ImmutableSet.of();
   }

   @Override
   public Iterable<Image> listImages() {
      return ImmutableSet.of();
   }

   @Override
   public Iterable<NodeMetadata> listNodes() {
      return transform(nodes.get().asMap().values(), converter);
   }

   @Override
   public Iterable<NodeMetadata> listNodesByIds(Iterable<String> ids) {
      return transform(filterKeys(nodes.get().asMap(), in(ImmutableSet.copyOf(ids))).values(), converter);
   }

   @Override
   public Iterable<Location> listLocations() {
      return buildLocations();
   }

   private Iterable<Location> buildLocations() {
      Builder<Location> locations = ImmutableSet.builder();
      Location provider = getOnlyElement(locationSupplier.get());
      Set<String> zones = getZones();
      if (zones.isEmpty()) {
         locations.add(provider);
      } else {
         zones.forEach(zone -> locations.add(createLocation(zone, provider)));
      }
      return locations.build();
   }

   private Set<String> getZones() {
      return ImmutableSet.copyOf(filter(transform(nodes.get().asMap().values(),
            Node::getLocationId), Predicates.notNull()));
   }

   private Location createLocation(String zone, Location provider) {
      return new LocationBuilder().scope(LocationScope.ZONE).id(zone).description(zone).parent(provider).build();
   }

   @Override
   public NodeMetadata getNode(String id) {
      return getNodeMetadata(id);
   }

   private NodeMetadata getNodeMetadata(String id) {
      try {
         Node node = nodes.get().getUnchecked(id);
         return node != null ? converter.apply(node) : null;
      } catch (UncheckedExecutionException e) {
         return null;
      }
   }

   @Override
   public Image getImage(final String id) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }

   @Override
   public void destroyNode(final String id) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }

   @Override
   public void rebootNode(String id) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }

   @Override
   public void resumeNode(String id) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }

   @Override
   public void suspendNode(String id) {
      throw new UnsupportedOperationException("Method not implemented yet.");
   }
}
