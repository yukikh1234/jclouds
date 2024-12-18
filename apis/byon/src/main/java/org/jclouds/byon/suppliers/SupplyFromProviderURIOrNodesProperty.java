
package org.jclouds.byon.suppliers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import jakarta.annotation.Resource;

import org.jclouds.location.Provider;
import org.jclouds.logging.Logger;
import org.jclouds.util.Strings2;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SupplyFromProviderURIOrNodesProperty extends ByteSource implements Function<URI, InputStream> {
   @Resource
   protected Logger logger = Logger.NULL;
   private final Supplier<URI> url;

   @Inject(optional = true)
   @Named("byon.nodes")
   @VisibleForTesting
   String nodes;
   
   @VisibleForTesting
   public SupplyFromProviderURIOrNodesProperty(URI url) {
      this(Suppliers.ofInstance(checkNotNull(url, "url")));
   }
   
   @Inject
   public SupplyFromProviderURIOrNodesProperty(@Provider Supplier<URI> url) {
      this.url = checkNotNull(url, "url");
   }

   @Override
   public InputStream openStream() {
      if (nodes != null) {
         return Strings2.toInputStream(nodes);
      }
      return getStreamFromURI(url.get());
   }

   @Override
   public String toString() {
      return "[url=" + url + "]";
   }

   @Override
   public InputStream apply(URI input) {
      return getStreamFromURI(input);
   }

   private InputStream getStreamFromURI(URI uri) {
      try {
         if (uri.getScheme() != null && uri.getScheme().equals("classpath")) {
            return getClass().getResourceAsStream(uri.getPath());
         }
         return uri.toURL().openStream();
      } catch (IOException e) {
         logErrorAndThrow(uri, e);
         return null; // This line will never be reached, but is needed for compilation
      }
   }

   private void logErrorAndThrow(URI uri, IOException e) {
      logger.error(e, "URI could not be read: %s", uri);
      throw Throwables.propagate(e);
   }
}
