
package org.jclouds.openstack.keystone.v2_0.catalog;

import static org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface.ADMIN;
import static org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface.INTERNAL;
import static org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface.PUBLIC;
import static org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface.UNRECOGNIZED;

import java.net.URI;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.logging.Logger;
import org.jclouds.openstack.keystone.auth.domain.AuthInfo;
import org.jclouds.openstack.keystone.catalog.ServiceEndpoint;
import org.jclouds.openstack.keystone.catalog.ServiceEndpoint.Interface;
import org.jclouds.openstack.keystone.v2_0.domain.Access;
import org.jclouds.openstack.keystone.v2_0.domain.Endpoint;
import org.jclouds.openstack.keystone.v2_0.domain.Service;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;

@Singleton
public class V2ServiceCatalog implements Supplier<List<ServiceEndpoint>> {

   @Resource
   private Logger logger = Logger.NULL;

   private final Supplier<AuthInfo> authInfo;

   @Inject
   V2ServiceCatalog(Supplier<AuthInfo> authInfo) {
      this.authInfo = authInfo;
   }

   @Override
   public List<ServiceEndpoint> get() {
      Access access = (Access) authInfo.get();
      ImmutableList.Builder<ServiceEndpoint> serviceEndpoints = ImmutableList.builder();
      for (Service service : access) {
         for (Endpoint endpoint : service) {
            processEndpoint(service.getType(), endpoint, serviceEndpoints);
         }
      }
      return serviceEndpoints.build();
   }

   private void processEndpoint(String type, Endpoint endpoint, ImmutableList.Builder<ServiceEndpoint> serviceEndpoints) {
      addIfNotNull(serviceEndpoints, endpoint.getAdminURL(), toServiceEndpoint(type, ADMIN), endpoint);
      addIfNotNull(serviceEndpoints, endpoint.getInternalURL(), toServiceEndpoint(type, INTERNAL), endpoint);
      addIfNotNull(serviceEndpoints, endpoint.getPublicURL(), toServiceEndpoint(type, PUBLIC), endpoint);
   }

   private void addIfNotNull(ImmutableList.Builder<ServiceEndpoint> builder, URI url, Function<Endpoint, ServiceEndpoint> function, Endpoint endpoint) {
      if (url != null) {
         builder.add(function.apply(endpoint));
      }
   }

   private Function<Endpoint, ServiceEndpoint> toServiceEndpoint(final String type, final Interface iface) {
      return endpoint -> buildServiceEndpoint(type, iface, endpoint);
   }

   private ServiceEndpoint buildServiceEndpoint(String type, Interface iface, Endpoint input) {
      ServiceEndpoint.Builder builder = ServiceEndpoint.builder()
            .id(input.getId())
            .iface(iface)
            .regionId(input.getRegion())
            .type(type)
            .version(input.getVersionId());

      URI url = getUrlForInterface(iface, input);
      if (iface == UNRECOGNIZED) {
         logger.warn("Unrecognized endpoint interface for %s. Using URL: %s", input, url);
      }
      builder.url(url);
      return builder.build();
   }

   private URI getUrlForInterface(Interface iface, Endpoint input) {
      switch (iface) {
         case ADMIN:
            return input.getAdminURL();
         case INTERNAL:
            return input.getInternalURL();
         case PUBLIC:
            return input.getPublicURL();
         default:
            return input.getPublicURL() != null ? input.getPublicURL() : input.getInternalURL();
      }
   }
}
