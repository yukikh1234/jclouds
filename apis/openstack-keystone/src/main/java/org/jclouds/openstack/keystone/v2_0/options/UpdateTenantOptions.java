
package org.jclouds.openstack.keystone.v2_0.options;

import static com.google.common.base.Objects.equal;

import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;

public class UpdateTenantOptions implements MapBinder {
   @Inject
   private BindToJsonPayload jsonBinder;

   private String name;
   private String description;
   private boolean enabled;

   @Override
   public boolean equals(Object object) {
      if (this == object) {
         return true;
      }
      if (object instanceof UpdateTenantOptions) {
         final UpdateTenantOptions other = UpdateTenantOptions.class.cast(object);
         return equal(description, other.description) && equal(enabled, other.enabled) && equal(name, other.name);
      }
      return false;
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(name, description, enabled);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
              .omitNullValues()
              .add("name", name)
              .add("description", description)
              .add("enabled", enabled)
              .toString();
   }

   static class ServerRequest {
      String name;
      String description;
      boolean enabled;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      return bindToRequest(request, createServerRequest());
   }

   private ServerRequest createServerRequest() {
      ServerRequest tenant = new ServerRequest();
      tenant.description = description;
      tenant.name = name;
      tenant.enabled = enabled;
      return tenant;
   }

   public String getDescription() {
      return this.description;
   }

   public UpdateTenantOptions description(String description) {
      this.description = description;
      return this;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public UpdateTenantOptions enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public UpdateTenantOptions name(String name) {
      this.name = name;
      return this;
   }

   public String getName() {
      return this.name;
   }

   public static class Builder {
      public static UpdateTenantOptions name(String name) {
         return new UpdateTenantOptions().name(name);
      }

      public static UpdateTenantOptions description(String description) {
         return new UpdateTenantOptions().description(description);
      }

      public static UpdateTenantOptions enabled(boolean enabled) {
         return new UpdateTenantOptions().enabled(enabled);
      }
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Object input) {
      return jsonBinder.bindToRequest(request, input);
   }
}
