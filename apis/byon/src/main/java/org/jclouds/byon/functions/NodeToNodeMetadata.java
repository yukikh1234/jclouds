
package org.jclouds.byon.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Optional;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jclouds.byon.Node;
import org.jclouds.collect.Memoized;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeMetadataBuilder;
import org.jclouds.compute.domain.OperatingSystem;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.NodeMetadata.Status;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Credentials;
import org.jclouds.domain.Location;
import org.jclouds.domain.LoginCredentials;
import org.jclouds.domain.LoginCredentials.Builder;
import org.jclouds.logging.Logger;
import org.jclouds.util.Strings2;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;

@Singleton
public class NodeToNodeMetadata implements Function<Node, NodeMetadata> {
   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   protected Logger logger = Logger.NULL;

   private final Supplier<Location> location;
   private final Supplier<Set<? extends Location>> locations;
   private final Map<String, Credentials> credentialStore;
   private final Function<URI, InputStream> slurp;

   @Inject
   NodeToNodeMetadata(Supplier<Location> location, @Memoized Supplier<Set<? extends Location>> locations,
            Function<URI, InputStream> slurp, Map<String, Credentials> credentialStore) {
      this.location = checkNotNull(location, "location");
      this.locations = checkNotNull(locations, "locations");
      this.credentialStore = checkNotNull(credentialStore, "credentialStore");
      this.slurp = checkNotNull(slurp, "slurp");
   }

   @Override
   public NodeMetadata apply(Node from) {
      NodeMetadataBuilder builder = new NodeMetadataBuilder();
      populateBasicDetails(builder, from);
      populateOperatingSystem(builder, from);
      populateCredentials(builder, from);
      return builder.build();
   }

   private void populateBasicDetails(NodeMetadataBuilder builder, Node from) {
      builder.ids(from.getId());
      builder.name(from.getName());
      builder.loginPort(from.getLoginPort());
      builder.hostname(from.getHostname());
      builder.location(findLocationWithId(from.getLocationId()).orElse(location.get()));
      builder.group(from.getGroup());
      builder.tags(from.getTags());
      builder.userMetadata(from.getMetadata());
      builder.status(Status.RUNNING);
      builder.publicAddresses(ImmutableSet.of(from.getHostname()));
   }

   private void populateOperatingSystem(NodeMetadataBuilder builder, Node from) {
      builder.operatingSystem(OperatingSystem.builder()
               .arch(from.getOsArch())
               .family(OsFamily.fromValue(from.getOsFamily()))
               .description(from.getOsDescription())
               .version(from.getOsVersion())
               .build());
   }

   private void populateCredentials(NodeMetadataBuilder builder, Node from) {
      if (from.getUsername() == null) return;

      Builder credBuilder = LoginCredentials.builder().user(from.getUsername());
      loadCredential(from, credBuilder);
      if (from.getSudoPassword() != null) {
         credBuilder.password(from.getSudoPassword());
         credBuilder.authenticateSudo(true);
      }
      LoginCredentials creds = credBuilder.build();
      builder.credentials(creds);
      credentialStore.put("node#" + from.getId(), creds);
   }

   private void loadCredential(Node from, Builder credBuilder) {
      if (from.getCredentialUrl() != null) {
         try {
            credBuilder.credential(Strings2.toStringAndClose(slurp.apply(from.getCredentialUrl())));
         } catch (IOException e) {
            logger.error(e, "URI could not be read: %s", from.getCredentialUrl());
         }
      } else if (from.getCredential() != null) {
         credBuilder.credential(from.getCredential());
      }
   }

   private Optional<Location> findLocationWithId(final String locationId) {
      if (locationId == null) return Optional.empty();
      try {
         return Optional.of(Iterables.find(locations.get(), new Predicate<Location>() {
            @Override
            public boolean apply(Location input) {
               return input.getId().equals(locationId);
            }
         }));
      } catch (NoSuchElementException e) {
         logger.debug("couldn't match instance location %s in: %s", locationId, locations.get());
         return Optional.empty();
      }
   }
}
