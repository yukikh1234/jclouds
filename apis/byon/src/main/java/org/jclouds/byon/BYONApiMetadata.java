
package org.jclouds.byon;

import java.net.URI;

import org.jclouds.JcloudsVersion;
import org.jclouds.apis.ApiMetadata;
import org.jclouds.apis.internal.BaseApiMetadata;
import org.jclouds.byon.config.BYONComputeServiceContextModule;
import org.jclouds.byon.config.YamlNodeStoreModule;
import org.jclouds.compute.ComputeServiceContext;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * Implementation of {@link ApiMetadata} for jclouds BYON API
 * 
 * <h3>note</h3>
 * 
 * This class is not setup to allow a subclasses to override the type of api,
 * asyncapi, or context. This is an optimization for s.
 */
@AutoService(ApiMetadata.class)
public class BYONApiMetadata extends BaseApiMetadata {

   @Override
   public Builder toBuilder() {
      return new Builder().fromApiMetadata(this);
   }

   public BYONApiMetadata() {
      this(new Builder());
   }

   protected BYONApiMetadata(Builder builder) {
      super(builder);
   }

   public static class Builder extends BaseApiMetadata.Builder<Builder> {

      protected Builder() {
         initBuilder();
      }
      
      private void initBuilder() {
          id("byon")
          .name("Bring Your Own Node (BYON) API")
          .identityName("Unused")
          .defaultIdentity("foo")
          .defaultCredential("bar")
          .defaultEndpoint("file://byon.yaml")
          .documentation(URI.create("https://github.com/jclouds/jclouds/tree/master/apis/byon"))
          .version(getVersion())
          .buildVersion(JcloudsVersion.get().toString())
          .view(ComputeServiceContext.class)
          .defaultModules(getDefaultModules());
      }

      private String getVersion() {
          return String.format("%s.%s", JcloudsVersion.get().majorVersion, JcloudsVersion.get().minorVersion);
      }
      
      private ImmutableSet<Class<? extends Module>> getDefaultModules() {
          return ImmutableSet.of(YamlNodeStoreModule.class, BYONComputeServiceContextModule.class);
      }

      @Override
      public BYONApiMetadata build() {
         return new BYONApiMetadata(this);
      }

      @Override
      protected Builder self() {
         return this;
      }
   }
}
