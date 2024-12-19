

package org.jclouds.docker.domain;

import java.util.List;
import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_ContainerSummary extends ContainerSummary {

  private final String id;
  private final List<String> names;
  private final String created;
  private final String image;
  private final String command;
  private final List<Port> ports;
  private final String status;

  AutoValue_ContainerSummary(
      String id,
      List<String> names,
      String created,
      String image,
      String command,
      List<Port> ports,
      String status) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    if (names == null) {
      throw new NullPointerException("Null names");
    }
    this.names = names;
    if (created == null) {
      throw new NullPointerException("Null created");
    }
    this.created = created;
    if (image == null) {
      throw new NullPointerException("Null image");
    }
    this.image = image;
    if (command == null) {
      throw new NullPointerException("Null command");
    }
    this.command = command;
    if (ports == null) {
      throw new NullPointerException("Null ports");
    }
    this.ports = ports;
    if (status == null) {
      throw new NullPointerException("Null status");
    }
    this.status = status;
  }

  @Override
  public String id() {
    return id;
  }

  @Override
  public List<String> names() {
    return names;
  }

  @Override
  public String created() {
    return created;
  }

  @Override
  public String image() {
    return image;
  }

  @Override
  public String command() {
    return command;
  }

  @Override
  public List<Port> ports() {
    return ports;
  }

  @Override
  public String status() {
    return status;
  }

  @Override
  public String toString() {
    return "ContainerSummary{"
         + "id=" + id + ", "
         + "names=" + names + ", "
         + "created=" + created + ", "
         + "image=" + image + ", "
         + "command=" + command + ", "
         + "ports=" + ports + ", "
         + "status=" + status
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof ContainerSummary) {
      ContainerSummary that = (ContainerSummary) o;
      return (this.id.equals(that.id()))
           && (this.names.equals(that.names()))
           && (this.created.equals(that.created()))
           && (this.image.equals(that.image()))
           && (this.command.equals(that.command()))
           && (this.ports.equals(that.ports()))
           && (this.status.equals(that.status()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= names.hashCode();
    h$ *= 1000003;
    h$ ^= created.hashCode();
    h$ *= 1000003;
    h$ ^= image.hashCode();
    h$ *= 1000003;
    h$ ^= command.hashCode();
    h$ *= 1000003;
    h$ ^= ports.hashCode();
    h$ *= 1000003;
    h$ ^= status.hashCode();
    return h$;
  }

}
