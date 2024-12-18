
package org.jclouds.cloudwatch.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @see <a href="http://docs.amazonwebservices.com/AmazonCloudWatch/latest/APIReference/API_Dimension.html" />
 */
public class Dimension {

   private final String name;
   private final String value;

   public Dimension(String name, String value) {
      this.name = name;
      this.value = value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (!(obj instanceof Dimension)) {
         return false;
      }
      Dimension other = (Dimension) obj;
      return Objects.equal(this.name, other.name) &&
             Objects.equal(this.value, other.value);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode() {
      return Objects.hashCode(name, value);
   }

   /**
    * return the dimension name.
    */
   public String getName() {
      return name;
   }

   /**
    * return the dimension value.
    */
   public String getValue() {
      return value;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("value", value).toString();
   }
}
