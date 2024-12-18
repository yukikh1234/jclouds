
package org.jclouds.s3.domain;

public class CanonicalUser {
   private String id;
   private String displayName;

   public CanonicalUser() {}

   public CanonicalUser(String id) {
      this.id = id;
   }

   public CanonicalUser(String id, String displayName) {
      this(id);
      this.displayName = displayName;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getDisplayName() {
      return displayName;
   }

   public void setDisplayName(String displayName) {
      this.displayName = displayName;
   }

   @Override
   public int hashCode() {
      return 31 + ((id == null) ? 0 : id.hashCode());
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) return true;
      if (!(obj instanceof CanonicalUser)) return false;
      CanonicalUser other = (CanonicalUser) obj;
      return (id == null ? other.id == null : id.equals(other.id));
   }
}
