
package org.jclouds.s3.domain;

import java.util.Date;
import java.util.Objects;

/**
 * System metadata of the S3Bucket
 */
public class BucketMetadata implements Comparable<BucketMetadata> {
   private final Date creationDate;
   private final String name;
   private final CanonicalUser owner;

   public BucketMetadata(String name, Date creationDate, CanonicalUser owner) {
      this.name = name;
      this.creationDate = creationDate;
      this.owner = owner;
   }

   /**
    * Every bucket and object in Amazon S3 has an owner, the user that created the bucket or object.
    * The owner of a bucket or object cannot be changed. However, if the object is overwritten by
    * another user (deleted and rewritten), the new object will have a new owner.
    */
   public CanonicalUser getOwner() {
      return owner;
   }

   public Date getCreationDate() {
      return creationDate;
   }

   /**
    * To comply with Amazon S3 requirements, bucket names must:
    * <p/>
    * Contain lowercase letters, numbers, periods (.), underscores (_), and dashes (-)
    * <p/>
    * Start with a number or letter
    * <p/>
    * Be between 3 and 255 characters long
    * <p/>
    * Not be in an IP address style (e.g., "192.168.5.4")
    */
   public String getName() {
      return name;
   }

   public int compareTo(BucketMetadata o) {
      return (this == o) ? 0 : getName().compareTo(o.getName());
   }

   @Override
   public int hashCode() {
      return Objects.hash(name, owner);
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }
      BucketMetadata other = (BucketMetadata) obj;
      return Objects.equals(name, other.name) && Objects.equals(owner, other.owner);
   }
}
