
package org.jclouds.openstack.keystone.v2_0.options;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableMap;

public class CreateTenantOptions implements MapBinder {
   @Inject
   private BindToJsonPayload jsonBinder;

   private String description;
   private boolean enabled;

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      }
      if (object instanceof CreateTenantOptions) {
         final CreateTenantOptions other = CreateTenantOptions.class.cast(object);
         return equal(description, other.description) && equal(enabled, other.enabled);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(description, enabled);
   }

   protected ToStringHelper string() {
      return MoreObjects.toStringHelper(this)
            .omitNullValues()
            .add("description", description)
            .add("enabled", enabled);
   }

   @Override
   public String toString() {
      return string().toString();
   }

   static class ServerRequest {
      final String name;
      String description;
      boolean enabled;

      private ServerRequest(String name) {
         this.name = name;
      }
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      ServerRequest tenant = createServerRequestFromParams(postParams);
      return bindToRequest(request, (Object) ImmutableMap.of("tenant", tenant));
   }

   private ServerRequest createServerRequestFromParams(Map<String, Object> postParams) {
      ServerRequest tenant = new ServerRequest(checkNotNull(postParams.get("name"), "name parameter not present").toString());
      if (description != null) {
         tenant.description = description;
      }
      tenant.enabled = enabled;
      return tenant;
   }

   public String getDescription() {
      return this.description;
   }

   public CreateTenantOptions description(String description) {
      this.description = description;
      return this;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public CreateTenantOptions enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public static class Builder {
      public static CreateTenantOptions description(String description) {
         return new CreateTenantOptions().description(description);
      }

      public static CreateTenantOptions enabled(boolean enabled) {
         return new CreateTenantOptions().enabled(enabled);
      }
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      return jsonBinder.bindToRequest(request, input);
   }
}
