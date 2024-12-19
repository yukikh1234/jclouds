
package org.jclouds.openstack.keystone.v2_0.functions.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import com.google.common.base.Optional;
import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.Arg0ToPagedIterable;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;
import org.jclouds.openstack.keystone.v2_0.KeystoneApi;
import org.jclouds.openstack.v2_0.domain.PaginatedCollection;
import org.jclouds.openstack.keystone.v2_0.domain.Tenant;
import org.jclouds.openstack.keystone.v2_0.features.TenantApi;
import org.jclouds.openstack.keystone.v2_0.functions.internal.ParseTenants.Tenants;
import org.jclouds.openstack.v2_0.domain.Link;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.inject.TypeLiteral;
import org.jclouds.openstack.v2_0.options.PaginationOptions;

@Beta
@Singleton
public class ParseTenants extends ParseJson<Tenants> {

   static class Tenants extends PaginatedCollection<Tenant> {
      @ConstructorProperties({ "tenants", "tenants_links" })
      protected Tenants(Iterable<Tenant> tenants, Iterable<Link> tenants_links) {
         super(tenants, tenants_links);
      }
   }

   @Inject
   public ParseTenants(Json json) {
      super(json, TypeLiteral.get(Tenants.class));
   }

   public static class ToPagedIterable extends Arg0ToPagedIterable.FromCaller<Tenant, ToPagedIterable> {

      private final KeystoneApi api;

      @Inject
      protected ToPagedIterable(KeystoneApi api) {
         this.api = checkNotNull(api, "api");
      }

      @Override
      protected Function<Object, IterableWithMarker<Tenant>> markerToNextForArg0(Optional<Object> ignored) {
         final TenantApi tenantApi = api.getTenantApi().get();
         return new TenantPaginationFunction(tenantApi);
      }

      private static class TenantPaginationFunction implements Function<Object, IterableWithMarker<Tenant>> {
         private final TenantApi tenantApi;

         private TenantPaginationFunction(TenantApi tenantApi) {
            this.tenantApi = tenantApi;
         }

         @SuppressWarnings("unchecked")
         @Override
         public IterableWithMarker<Tenant> apply(Object input) {
            PaginationOptions paginationOptions = PaginationOptions.class.cast(input);
            return IterableWithMarker.class.cast(tenantApi.list(paginationOptions));
         }

         @Override
         public String toString() {
            return "listTenants()";
         }
      }
   }
}
