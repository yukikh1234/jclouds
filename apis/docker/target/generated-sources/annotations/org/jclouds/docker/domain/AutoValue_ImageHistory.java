

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ImageHistory extends ImageHistory {

  private final String id;
  private final long created;
  private final String createdBy;
  private final List<String> tags;
  private final long size;
  private final String comment;

  AutoValue_ImageHistory(
      String id,
      long created,
      String createdBy,
      @Nullable List<String> tags,
      long size,
      String comment) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.created = created;
    if (createdBy == null) {
      throw new NullPointerException("Null createdBy");
    }
    this.createdBy = createdBy;
    this.tags = tags;
    this.size = size;
    if (comment == null) {
      throw new NullPointerException("Null comment");
    }
    this.comment = comment;
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
  public String createdBy() {
    return createdBy;
  }

  @Nullable
  @Override
  public List<String> tags() {
    return tags;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public String comment() {
    return comment;
  }

  @Override
  public String toString() {
    return "ImageHistory{"
         + "id=" + id + ", "
         + "created=" + created + ", "
         + "createdBy=" + createdBy + ", "
         + "tags=" + tags + ", "
         + "size=" + size + ", "
         + "comment=" + comment
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ImageHistory) {
      ImageHistory that = (ImageHistory) o;
      return (this.id.equals(that.id()))
           && (this.created == that.created())
           && (this.createdBy.equals(that.createdBy()))
           && ((this.tags == null) ? (that.tags() == null) : this.tags.equals(that.tags()))
           && (this.size == that.size())
           && (this.comment.equals(that.comment()));
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
    h$ ^= createdBy.hashCode();
    h$ *= 1000003;
    h$ ^= (tags == null) ? 0 : tags.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((size >>> 32) ^ size);
    h$ *= 1000003;
    h$ ^= comment.hashCode();
    return h$;
  }

}
