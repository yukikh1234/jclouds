

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;
import org.jclouds.javax.annotation.Nullable;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_ProjectIdScope_ProjectId extends Auth.ProjectIdScope.ProjectId {

  private final String id;
  private final Object domain;

  AutoValue_Auth_ProjectIdScope_ProjectId(
      String id,
      @Nullable Object domain) {
    if (id == null) {
      throw new NullPointerException("Null id");
    }
    this.id = id;
    this.domain = domain;
  }

  @Override
  public String id() {
    return id;
  }

  @Nullable
  @Override
  public Object domain() {
    return domain;
  }

  @Override
  public String toString() {
    return "ProjectId{"
         + "id=" + id + ", "
         + "domain=" + domain
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.ProjectIdScope.ProjectId) {
      Auth.ProjectIdScope.ProjectId that = (Auth.ProjectIdScope.ProjectId) o;
      return (this.id.equals(that.id()))
           && ((this.domain == null) ? (that.domain() == null) : this.domain.equals(that.domain()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= id.hashCode();
    h$ *= 1000003;
    h$ ^= (domain == null) ? 0 : domain.hashCode();
    return h$;
  }

}
