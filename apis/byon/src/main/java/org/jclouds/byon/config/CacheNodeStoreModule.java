
package org.jclouds.byon.config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.jclouds.byon.Node;

import com.google.common.annotations.Beta;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

@ConfiguresNodeStore
@Beta
public class CacheNodeStoreModule extends AbstractModule {
   private final LoadingCache<String, Node> backing;

   private CacheNodeStoreModule(LoadingCache<String, Node> backing) {
      this.backing = checkNotNull(backing, "backing");
   }

   public static CacheNodeStoreModule createWithCache(LoadingCache<String, Node> backing) {
      return new CacheNodeStoreModule(backing);
   }

   public static CacheNodeStoreModule createWithMap(Map<String, Node> backing) {
      LoadingCache<String, Node> cache = CacheBuilder.newBuilder()
              .<String, Node>build(CacheLoader.from(Functions.forMap(backing)));
      CacheNodeStoreModule module = new CacheNodeStoreModule(cache);
      for (String node : backing.keySet()) {
         module.backing.getUnchecked(node);
      }
      return module;
   }

   @Override
   protected void configure() {
      bind(new TypeLiteral<LoadingCache<String, Node>>() {}).toInstance(backing);
      bind(new TypeLiteral<Supplier<LoadingCache<String, Node>>>() {}).toInstance(Suppliers.ofInstance(backing));
   }
}
