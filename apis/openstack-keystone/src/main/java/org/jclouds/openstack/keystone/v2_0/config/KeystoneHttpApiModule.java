
package org.jclouds.openstack.keystone.v2_0.config;

import java.net.URI;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jakarta.inject.Singleton;

import org.jclouds.http.HttpErrorHandler;
import org.jclouds.http.annotation.ClientError;
import org.jclouds.http.annotation.Redirection;
import org.jclouds.http.annotation.ServerError;
import org.jclouds.openstack.keystone.v2_0.KeystoneApi;
import org.jclouds.openstack.keystone.v2_0.handlers.KeystoneErrorHandler;
import org.jclouds.openstack.v2_0.domain.Extension;
import org.jclouds.openstack.v2_0.functions.PresentWhenExtensionAnnotationMatchesExtensionSet;
import org.jclouds.rest.ConfiguresHttpApi;
import org.jclouds.rest.config.HttpApiModule;
import org.jclouds.rest.functions.ImplicitOptionalConverter;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;

/**
 * Configures the Keystone API.
 */
@ConfiguresHttpApi
public class KeystoneHttpApiModule extends HttpApiModule<KeystoneApi> {

   public KeystoneHttpApiModule() {
   }

   public static MapBinder<URI, URI> namespaceAliasBinder(Binder binder) {
      return MapBinder.newMapBinder(binder, URI.class, URI.class, NamespaceAliases.class).permitDuplicates();
   }

   @Override
   protected void configure() {
      bind(ImplicitOptionalConverter.class).to(PresentWhenExtensionAnnotationMatchesExtensionSet.class);
      super.configure();
      namespaceAliasBinder(binder());
   }

   @Provides
   @Singleton
   public final LoadingCache<String, Set<? extends Extension>> provideExtensionsByRegion(final jakarta.inject.Provider<KeystoneApi> keystoneApi) {
      return createCache(keystoneApi);
   }

   private LoadingCache<String, Set<? extends Extension>> createCache(final jakarta.inject.Provider<KeystoneApi> keystoneApi) {
      return CacheBuilder.newBuilder().expireAfterWrite(23, TimeUnit.HOURS)
            .build(CacheLoader.from(createSupplier(keystoneApi)));
   }

   private Supplier<Set<? extends Extension>> createSupplier(final jakarta.inject.Provider<KeystoneApi> keystoneApi) {
      return Suppliers.memoize(new Supplier<Set<? extends Extension>>() {
         @Override
         public Set<? extends Extension> get() {
            return keystoneApi.get().getExtensionApi().list();
         }
      });
   }

   @Override
   protected void bindErrorHandlers() {
      bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(KeystoneErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(KeystoneErrorHandler.class);
      bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(KeystoneErrorHandler.class);
   }
}
