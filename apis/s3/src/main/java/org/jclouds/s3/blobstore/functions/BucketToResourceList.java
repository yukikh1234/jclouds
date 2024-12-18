
package org.jclouds.s3.blobstore.functions;

import java.util.SortedSet;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jclouds.blobstore.domain.PageSet;
import org.jclouds.blobstore.domain.StorageMetadata;
import org.jclouds.blobstore.domain.internal.PageSetImpl;
import org.jclouds.s3.domain.ListBucketResponse;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

@Singleton
public class BucketToResourceList implements Function<ListBucketResponse, PageSet<? extends StorageMetadata>> {
   private final ObjectToBlobMetadata object2blobMd;
   private final CommonPrefixesToResourceMetadata prefix2ResourceMd;

   @Inject
   public BucketToResourceList(ObjectToBlobMetadata object2blobMd, CommonPrefixesToResourceMetadata prefix2ResourceMd) {
      this.object2blobMd = object2blobMd;
      this.prefix2ResourceMd = prefix2ResourceMd;
   }

   @Override
   public PageSet<? extends StorageMetadata> apply(ListBucketResponse from) {
      SortedSet<StorageMetadata> contents = Sets.newTreeSet(Iterables.transform(from, object2blobMd));
      addCommonPrefixes(from, contents);
      return new PageSetImpl<>(contents, from.getNextMarker());
   }

   private void addCommonPrefixes(ListBucketResponse from, SortedSet<StorageMetadata> contents) {
      for (String prefix : from.getCommonPrefixes()) {
         contents.add(prefix2ResourceMd.apply(prefix));
      }
   }
}
