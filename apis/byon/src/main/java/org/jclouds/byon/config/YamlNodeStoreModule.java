
package org.jclouds.byon.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.inject.Named;
import jakarta.inject.Singleton;

import org.jclouds.byon.Node;
import org.jclouds.byon.domain.YamlNode;
import org.jclouds.byon.functions.NodesFromYamlStream;
import org.jclouds.byon.suppliers.NodesParsedFromSupplier;
import org.jclouds.collect.TransformingMap;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.ByteSource;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;

@ConfiguresNodeStore
@Beta
public class YamlNodeStoreModule extends AbstractModule {
   private static final Map<String, ByteSource> BACKING = new ConcurrentHashMap<>();
   private final Map<String, ByteSource> backing;

   public YamlNodeStoreModule(Map<String, ByteSource> backing) {
      this.backing = backing;
   }

   public YamlNodeStoreModule() {
      this(null);
   }

   @Override
   protected void configure() {
      bindSupplier();
      bindFunctions();
      bindBackingMap();
   }

   private void bindSupplier() {
      bind(new TypeLiteral<Supplier<LoadingCache<String, Node>>>() {
      }).to(NodesParsedFromSupplier.class);
   }

   private void bindFunctions() {
      bind(new TypeLiteral<Function<ByteSource, LoadingCache<String, Node>>>() {
      }).to(NodesFromYamlStream.class);
      bindYamlNodeFunctions();
      bindNodeFunctions();
   }

   private void bindYamlNodeFunctions() {
      bind(new TypeLiteral<Function<YamlNode, ByteSource>>() {
      }).toInstance(org.jclouds.byon.domain.YamlNode.yamlNodeToByteSource);
      bind(new TypeLiteral<Function<ByteSource, YamlNode>>() {
      }).toInstance(org.jclouds.byon.domain.YamlNode.byteSourceToYamlNode);
   }

   private void bindNodeFunctions() {
      bind(new TypeLiteral<Function<Node, YamlNode>>() {
      }).toInstance(org.jclouds.byon.domain.YamlNode.nodeToYamlNode);
      bind(new TypeLiteral<Function<YamlNode, Node>>() {
      }).toInstance(org.jclouds.byon.domain.YamlNode.toNode);
   }

   private void bindBackingMap() {
      if (backing != null) {
         bind(new TypeLiteral<Map<String, ByteSource>>() {
         }).annotatedWith(Names.named("yaml")).toInstance(backing);
      } else {
         bind(new TypeLiteral<Map<String, ByteSource>>() {
         }).annotatedWith(Names.named("yaml")).toInstance(BACKING);
      }
   }

   @Provides
   @Singleton
   protected final LoadingCache<String, Node> provideNodeStore(Map<String, YamlNode> backing, 
         Function<Node, YamlNode> yamlSerializer, Function<YamlNode, Node> yamlDeserializer) {
      return CacheBuilder.newBuilder().build(CacheLoader.from(
            Functions.forMap(new TransformingMap<>(backing, yamlDeserializer, yamlSerializer))));
   }

   @Provides
   @Singleton
   protected final Map<String, YamlNode> provideYamlStore(@Named("yaml") Map<String, ByteSource> backing,
         Function<YamlNode, ByteSource> yamlSerializer, Function<ByteSource, YamlNode> yamlDeserializer) {
      return new TransformingMap<>(backing, yamlDeserializer, yamlSerializer);
   }
}
