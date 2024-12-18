
package org.jclouds.s3.predicates.validators;

import jakarta.inject.Inject;
import org.jclouds.predicates.Validator;
import com.google.common.base.CharMatcher;
import com.google.inject.Singleton;

/**
 * Validates name for S3 buckets. The complete requirements are listed at:
 * http://docs.amazonwebservices.com/AmazonS3/latest/index.html?BucketRestrictions.html
 * 
 * @see org.jclouds.rest.InputParamValidator
 * @see org.jclouds.predicates.Validator
 */
@Singleton
public class BucketNameValidator extends Validator<String> {
   private static final CharMatcher MATCHER =
         CharMatcher.inRange('a', 'z')
         .or(CharMatcher.inRange('A', 'Z'))
         .or(CharMatcher.inRange('0', '9'))
         .or(CharMatcher.anyOf(".-_"));

   @Inject
   public BucketNameValidator() {
   }

   @Override
   public void validate(String name) {
      if (isNull(name)) {
         throw exception("", "Can't be null");
      }
      if (isTooShort(name)) {
         throw exception(name, "Can't be less than 3 characters");
      }
      if (isTooLong(name)) {
         throw exception(name, "Can't be over 255 characters");
      }
      if (hasIllegalCharacters(name)) {
         throw exception(name, "Illegal character");
      }
   }

   private boolean isNull(String name) {
      return name == null;
   }

   private boolean isTooShort(String name) {
      return name.length() < 3;
   }

   private boolean isTooLong(String name) {
      return name.length() > 255;
   }

   private boolean hasIllegalCharacters(String name) {
      return !MATCHER.matchesAllOf(name);
   }

   private static IllegalArgumentException exception(String containerName, String reason) {
      return new IllegalArgumentException(String.format(
            "Object '%s' doesn't match S3 bucket naming convention. " +
            "Reason: %s. For more info, please refer to https://docs.aws.amazon.com/AmazonS3/latest/dev/BucketRestrictions.html",
            containerName, reason));
   }
}
