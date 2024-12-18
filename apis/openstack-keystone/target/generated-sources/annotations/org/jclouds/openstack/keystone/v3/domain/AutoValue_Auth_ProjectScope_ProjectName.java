

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_ProjectScope_ProjectName extends Auth.ProjectScope.ProjectName {

  private final String name;
  private final Object domain;

  AutoValue_Auth_ProjectScope_ProjectName(
      String name,
      @Nullable Object domain) {
    if (name == null) {
      throw new NullPointerException("Null name");
    }
    this.name = name;
    this.domain = domain;
  }

  @Override
  public String name() {
    return name;
  }

  @Nullable
  @Override
  public Object domain() {
    return domain;
  }

  @Override
  public String toString() {
    return "ProjectName{"
         + "name=" + name + ", "
         + "domain=" + domain
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.ProjectScope.ProjectName) {
      Auth.ProjectScope.ProjectName that = (Auth.ProjectScope.ProjectName) o;
      return (this.name.equals(that.name()))
           && ((this.domain == null) ? (that.domain() == null) : this.domain.equals(that.domain()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= name.hashCode();
    h$ *= 1000003;
    h$ ^= (domain == null) ? 0 : domain.hashCode();
    return h$;
  }

}
