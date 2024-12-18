

package org.jclouds.openstack.keystone.v3.domain;

import javax.annotation.processing.Generated;

@Generated("com.google.auto.value.processor.AutoValueProcessor")
 final class AutoValue_Auth_ProjectIdScope extends Auth.ProjectIdScope {

  private final Auth.ProjectIdScope.ProjectId project;

  AutoValue_Auth_ProjectIdScope(
      Auth.ProjectIdScope.ProjectId project) {
    if (project == null) {
      throw new NullPointerException("Null project");
    }
    this.project = project;
  }

  @Override
  public Auth.ProjectIdScope.ProjectId project() {
    return project;
  }

  @Override
  public String toString() {
    return "ProjectIdScope{"
         + "project=" + project
        + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o instanceof Auth.ProjectIdScope) {
      Auth.ProjectIdScope that = (Auth.ProjectIdScope) o;
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
