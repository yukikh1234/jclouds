
package org.jclouds.s3.domain;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ForwardingSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Multi-object delete API response
 * <p/>
 * Contains a list of the keys that were deleted
 */
public class DeleteResult extends ForwardingSet<String> {

   public static class Error {

      private final String code;
      private final String message;

      public Error(String code, String message) {
         this.code = checkNotNull(code, "code is null");
         this.message = checkNotNull(message, "message is null");
      }

      public String getCode() {
         return code;
      }

      public String getMessage() {
         return message;
      }

      @Override
      public boolean equals(Object o) {
         return this == o || (o instanceof Error && equalsHelper((Error) o));
      }

      private boolean equalsHelper(Error that) {
         return Objects.equal(code, that.code) && Objects.equal(message, that.message);
      }

      @Override
      public int hashCode() {
         return Objects.hashCode(code, message);
      }

      @Override
      public String toString() {
         return MoreObjects.toStringHelper(this).omitNullValues()
            .add("code", code).add("message", message).toString();
      }
   }

   public static Builder builder() {
      return new Builder();
   }

   public Builder toBuilder() {
      return builder().fromDeleteResult(this);
   }

   public static class Builder {

      private final ImmutableSet.Builder<String> deleted = ImmutableSet.builder();
      private final ImmutableMap.Builder<String, Error> errors = ImmutableMap.builder();

      /**
       * @see DeleteResult#getErrors
       */
      public Builder putError(String key, Error error) {
         this.errors.put(key, error);
         return this;
      }

      /**
       * @see DeleteResult#getErrors
       */
      public Builder errors(Map<String, Error> errors) {
         this.errors.putAll(errors);
         return this;
      }

      /**
       * @see DeleteResult#getDeleted
       */
      public Builder deleted(Iterable<String> deleted) {
         this.deleted.addAll(deleted);
         return this;
      }

      /**
       * @see DeleteResult#getDeleted
       */
      public Builder add(String key) {
         this.deleted.add(key);
         return this;
      }

      /**
       * @see DeleteResult#getDeleted
       */
      public Builder addAll(Iterable<String> keys) {
         this.deleted.addAll(keys);
         return this;
      }

      public DeleteResult build() {
         return new DeleteResult(deleted.build(), errors.build());
      }

      public Builder fromDeleteResult(DeleteResult result) {
         return addAll(result.getDeleted()).errors(result.getErrors());
      }
   }

   private final Set<String> deleted;
   private final Map<String, Error> errors;

   public DeleteResult(Set<String> deleted, Map<String, Error> errors) {
      this.deleted = ImmutableSet.copyOf(deleted);
      this.errors = ImmutableMap.copyOf(errors);
   }

   /**
    * Get the set of successfully deleted keys
    */
   public Set<String> getDeleted() {
      return deleted;
   }

   /**
    * Get a map with details about failed delete operations indexed by object name
    */
   public Map<String, Error> getErrors() {
      return errors;
   }

   @Override
   protected Set<String> delegate() {
      return deleted;
   }

   @Override
   public boolean equals(Object o) {
      return this == o || (o instanceof DeleteResult && equalsHelper((DeleteResult) o));
   }

   private boolean equalsHelper(DeleteResult that) {
      return Objects.equal(errors, that.errors) && Objects.equal(deleted, that.deleted);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(deleted, errors);
   }

   @Override
   public String toString() {
      return MoreObjects.toStringHelper(this).omitNullValues()
         .add("deleted", deleted).add("errors", errors).toString();
   }
}
