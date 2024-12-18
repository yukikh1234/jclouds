
package org.jclouds.s3.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.jclouds.http.HttpRequest;
import org.jclouds.reflect.Reflection2;
import org.jclouds.rest.internal.GeneratedHttpRequest;
import org.jclouds.s3.Bucket;
import org.jclouds.s3.S3Client;

import com.google.common.base.Predicate;
import com.google.common.reflect.Parameter;

/**
 * Encryption, Hashing, and IO Utilities needed to sign and verify S3 requests and responses.
 */
public class S3Utils {

    private static final Pattern BUCKET_NAME_PATTERN = Pattern.compile("^[a-z0-9][-_.a-z0-9]+");
    private static final Pattern IP_PATTERN = Pattern.compile("b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).)"
            + "{3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)b");

    private static final Predicate<Annotation> ANNOTATIONTYPE_BUCKET = input -> input.annotationType().equals(Bucket.class);

    public static String getBucketName(HttpRequest req) {
        checkArgument(req instanceof GeneratedHttpRequest, "this should be a generated http request");
        GeneratedHttpRequest request = GeneratedHttpRequest.class.cast(req);

        List<Parameter> parameters = Reflection2.getInvokableParameters(request.getInvocation().getInvokable());
        for (int i = 0; i < parameters.size(); i++) {
            if (any(Arrays.asList(parameters.get(i).getAnnotations()), ANNOTATIONTYPE_BUCKET)) {
                return (String) request.getInvocation().getArgs().get(i);
            }
        }
        return null;
    }

    // TODO add validatorparam so that this is actually used
    public static String validateBucketName(String bucketName) {
        checkNotNull(bucketName, "bucketName");
        checkArgument(
            BUCKET_NAME_PATTERN.matcher(bucketName).matches(),
            "bucketName name must start with a number or letter and  can only contain lowercase letters, numbers, periods (.), underscores (_), and dashes (-)");
        checkArgument(bucketName.length() > 2 && bucketName.length() < 256,
            "bucketName name must be between 3 and 255 characters long");
        checkArgument(!IP_PATTERN.matcher(bucketName).matches(), "bucketName name cannot be ip address style");
        return bucketName;
    }

    /**
     * This implementation invokes {@link S3Client#deleteBucketIfEmpty} followed by {@link S3Client#bucketExists} until
     * it is true.
     */
    public static boolean deleteAndVerifyContainerGone(S3Client sync, String container) {
        sync.deleteBucketIfEmpty(container);
        return !sync.bucketExists(container);
    }
}
