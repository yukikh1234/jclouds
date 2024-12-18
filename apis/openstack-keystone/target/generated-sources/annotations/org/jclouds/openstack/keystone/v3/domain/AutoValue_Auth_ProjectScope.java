

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_ProjectScope extends Auth.ProjectScope {

  private final Auth.ProjectScope.ProjectName project;

  AutoValue_Auth_ProjectScope(
      Auth.ProjectScope.ProjectName project) {
    if (project == null) {
      throw new NullPointerException("Null project");
    }
    this.project = project;
  }

  @Override
  public Auth.ProjectScope.ProjectName project() {
    return project;
  }

  @Override
  public String toString() {
    return "ProjectScope{"
         + "project=" + project
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.ProjectScope) {
      Auth.ProjectScope that = (Auth.ProjectScope) o;
      return (this.project.equals(that.project()));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int h$ = 1;
    h$ *= 1000003;
    h$ ^= project.hashCode();
    return h$;
  }

}
