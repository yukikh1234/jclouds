

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ImageSummary extends ImageSummary {

  private final String id;
  private final long created;
  private final String parentId;
  private final long size;
  private final long virtualSize;
  private final List<String> repoTags;

  AutoValue_ImageSummary(
      String id,
      long created,
      String parentId,
      long size,
      long virtualSize,
      List<String> repoTags) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.created = created;
    if (parentId == null) {
      throw new NullPointerException("Null parentId");
    }
    this.parentId = parentId;
    this.size = size;
    this.virtualSize = virtualSize;
    if (repoTags == null) {
      throw new NullPointerException("Null repoTags");
    }
    this.repoTags = repoTags;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public long created() {
    return created;
  }

  @Override
  public String parentId() {
    return parentId;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public long virtualSize() {
    return virtualSize;
  }

  @Override
  public List<String> repoTags() {
    return repoTags;
  }

  @Override
  public String toString() {
    return "ImageSummary{"
         + "id=" + id + ", "
         + "created=" + created + ", "
         + "parentId=" + parentId + ", "
         + "size=" + size + ", "
         + "virtualSize=" + virtualSize + ", "
         + "repoTags=" + repoTags
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ImageSummary) {
      ImageSummary that = (ImageSummary) o;
      return (this.id.equals(that.id()))
           && (this.created == that.created())
           && (this.parentId.equals(that.parentId()))
           && (this.size == that.size())
           && (this.virtualSize == that.virtualSize())
           && (this.repoTags.equals(that.repoTags()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((created >>> 32) ^ created);
    h$ *= 1000003;
    h$ ^= parentId.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((size >>> 32) ^ size);
    h$ *= 1000003;
    h$ ^= (int) ((virtualSize >>> 32) ^ virtualSize);
    h$ *= 1000003;
    h$ ^= repoTags.hashCode();
    return h$;
  }

}
