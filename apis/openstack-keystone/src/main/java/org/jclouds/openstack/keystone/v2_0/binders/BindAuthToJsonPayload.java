
package org.jclouds.openstack.keystone.v2_0.binders;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Predicates.instanceOf;
import static com.google.common.collect.Iterables.tryFind;
import static org.jclouds.openstack.keystone.auth.config.CredentialTypes.findCredentialType;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.http.HttpRequest;
import org.jclouds.json.Json;
import org.jclouds.openstack.keystone.auth.config.CredentialType;
import org.jclouds.openstack.keystone.auth.domain.TenantOrDomainAndCredentials;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;
import org.jclouds.rest.internal.GeneratedHttpRequest;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

@Singleton
public class BindAuthToJsonPayload extends BindToJsonPayload implements MapBinder {
   @Inject
   public BindAuthToJsonPayload(Json jsonBinder) {
      super(jsonBinder);
   }

   protected TenantOrDomainAndCredentials<?> findCredentialsInArgs(GeneratedHttpRequest gRequest) {
      Optional<Object> credentials = tryFind(gRequest.getInvocation().getArgs(), instanceOf(TenantOrDomainAndCredentials.class));
      return credentials.isPresent() ? (TenantOrDomainAndCredentials<?>) credentials.get() : null;
   }

   @Override
   public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
      checkArgument(checkNotNull(request, "request") instanceof GeneratedHttpRequest,
            "this binder is only valid for GeneratedHttpRequests!");
      GeneratedHttpRequest gRequest = (GeneratedHttpRequest) request;
      Builder<String, Object> authDataBuilder = buildAuthData(gRequest);
      
      R authRequest = super.bindToRequest(request, ImmutableMap.of("auth", authDataBuilder.build()));
      authRequest.getPayload().setSensitive(true);
      return authRequest;
   }

   private Builder<String, Object> buildAuthData(GeneratedHttpRequest gRequest) {
      Builder<String, Object> builder = ImmutableMap.builder();
      TenantOrDomainAndCredentials<?> credentials = findCredentialsInArgs(gRequest);

      if (credentials != null) {
         CredentialType credentialType = findCredentialType(credentials.credentials().getClass());
         checkArgument(credentialType != null, "the given credentials must be annotated with @CredentialType");

         builder.put(credentialType.value(), credentials.credentials());
         addTenantInfo(builder, credentials);
      }
      return builder;
   }

   private void addTenantInfo(Builder<String, Object> builder, TenantOrDomainAndCredentials<?> credentials) {
      if (!Strings.isNullOrEmpty(credentials.tenantOrDomainId())) {
         builder.put("tenantId", credentials.tenantOrDomainId());
      } else if (!Strings.isNullOrEmpty(credentials.tenantOrDomainName())) {
         builder.put("tenantName", credentials.tenantOrDomainName());
      }
   }
}
