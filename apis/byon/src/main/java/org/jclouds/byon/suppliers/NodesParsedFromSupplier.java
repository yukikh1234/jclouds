
package org.jclouds.byon.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.byon.Node;
import org.jclouds.location.Provider;
import org.jclouds.logging.Logger;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.cache.LoadingCache;
import com.google.common.io.ByteSource;

@Singleton
public class NodesParsedFromSupplier implements Supplier<LoadingCache<String, Node>> {
   @Resource
   private Logger logger = Logger.NULL;

   private final ByteSource supplier;
   private final Function<ByteSource, LoadingCache<String, Node>> parser;

   @Inject
   public NodesParsedFromSupplier(@Provider ByteSource supplier, Function<ByteSource, LoadingCache<String, Node>> parser) {
      this.supplier = checkNotNull(supplier, "supplier");
      this.parser = checkNotNull(parser, "parser");
   }

   @Override
   public LoadingCache<String, Node> get() {
      LoadingCache<String, Node> nodes = parseNodes();
      validateNodes(nodes);
      return nodes;
   }

   private LoadingCache<String, Node> parseNodes() {
      return parser.apply(supplier);
   }

   private void validateNodes(LoadingCache<String, Node> nodes) {
      checkState(nodes != null && nodes.size() > 0, "no nodes parsed from supplier: %s", supplier);
   }
}
