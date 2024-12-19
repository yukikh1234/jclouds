

package org.jclouds.docker.domain;

import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Image extends Image {

  private final String id;
  private final String author;
  private final String comment;
  private final Config config;
  private final Config containerConfig;
  private final String parent;
  private final Date created;
  private final String container;
  private final String dockerVersion;
  private final String architecture;
  private final String os;
  private final long size;
  private final long virtualSize;
  private final List<String> repoTags;

  AutoValue_Image(
      String id,
      @Nullable String author,
      @Nullable String comment,
      @Nullable Config config,
      @Nullable Config containerConfig,
      String parent,
      Date created,
      String container,
      String dockerVersion,
      String architecture,
      String os,
      long size,
      long virtualSize,
      @Nullable List<String> repoTags) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.author = author;
    this.comment = comment;
    this.config = config;
    this.containerConfig = containerConfig;
    if (parent == null) {
      throw new NullPointerException("Null parent");
    }
    this.parent = parent;
    if (created == null) {
      throw new NullPointerException("Null created");
    }
    this.created = created;
    if (container == null) {
      throw new NullPointerException("Null container");
    }
    this.container = container;
    if (dockerVersion == null) {
      throw new NullPointerException("Null dockerVersion");
    }
    this.dockerVersion = dockerVersion;
    if (architecture == null) {
      throw new NullPointerException("Null architecture");
    }
    this.architecture = architecture;
    if (os == null) {
      throw new NullPointerException("Null os");
    }
    this.os = os;
    this.size = size;
    this.virtualSize = virtualSize;
    this.repoTags = repoTags;
  }

  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public String author() {
    return author;
  }

  @Nullable
  @Override
  public String comment() {
    return comment;
  }

  @Nullable
  @Override
  public Config config() {
    return config;
  }

  @Nullable
  @Override
  public Config containerConfig() {
    return containerConfig;
  }

  @Override
  public String parent() {
    return parent;
  }

  @Override
  public Date created() {
    return created;
  }

  @Override
  public String container() {
    return container;
  }

  @Override
  public String dockerVersion() {
    return dockerVersion;
  }

  @Override
  public String architecture() {
    return architecture;
  }

  @Override
  public String os() {
    return os;
  }

  @Override
  public long size() {
    return size;
  }

  @Override
  public long virtualSize() {
    return virtualSize;
  }

  @Nullable
  @Override
  public List<String> repoTags() {
    return repoTags;
  }

  @Override
  public String toString() {
    return "Image{"
         + "id=" + id + ", "
         + "author=" + author + ", "
         + "comment=" + comment + ", "
         + "config=" + config + ", "
         + "containerConfig=" + containerConfig + ", "
         + "parent=" + parent + ", "
         + "created=" + created + ", "
         + "container=" + container + ", "
         + "dockerVersion=" + dockerVersion + ", "
         + "architecture=" + architecture + ", "
         + "os=" + os + ", "
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
    if (o instanceof Image) {
      Image that = (Image) o;
      return (this.id.equals(that.id()))
           && ((this.author == null) ? (that.author() == null) : this.author.equals(that.author()))
           && ((this.comment == null) ? (that.comment() == null) : this.comment.equals(that.comment()))
           && ((this.config == null) ? (that.config() == null) : this.config.equals(that.config()))
           && ((this.containerConfig == null) ? (that.containerConfig() == null) : this.containerConfig.equals(that.containerConfig()))
           && (this.parent.equals(that.parent()))
           && (this.created.equals(that.created()))
           && (this.container.equals(that.container()))
           && (this.dockerVersion.equals(that.dockerVersion()))
           && (this.architecture.equals(that.architecture()))
           && (this.os.equals(that.os()))
           && (this.size == that.size())
           && (this.virtualSize == that.virtualSize())
           && ((this.repoTags == null) ? (that.repoTags() == null) : this.repoTags.equals(that.repoTags()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= (author == null) ? 0 : author.hashCode();
    h$ *= 1000003;
    h$ ^= (comment == null) ? 0 : comment.hashCode();
    h$ *= 1000003;
    h$ ^= (config == null) ? 0 : config.hashCode();
    h$ *= 1000003;
    h$ ^= (containerConfig == null) ? 0 : containerConfig.hashCode();
    h$ *= 1000003;
    h$ ^= parent.hashCode();
    h$ *= 1000003;
    h$ ^= created.hashCode();
    h$ *= 1000003;
    h$ ^= container.hashCode();
    h$ *= 1000003;
    h$ ^= dockerVersion.hashCode();
    h$ *= 1000003;
    h$ ^= architecture.hashCode();
    h$ *= 1000003;
    h$ ^= os.hashCode();
    h$ *= 1000003;
    h$ ^= (int) ((size >>> 32) ^ size);
    h$ *= 1000003;
    h$ ^= (int) ((virtualSize >>> 32) ^ virtualSize);
    h$ *= 1000003;
    h$ ^= (repoTags == null) ? 0 : repoTags.hashCode();
    return h$;
  }

}
