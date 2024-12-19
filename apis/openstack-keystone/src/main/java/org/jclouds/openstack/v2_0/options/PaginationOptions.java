
package org.jclouds.openstack.v2_0.options;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Date;

import org.jclouds.http.options.BaseHttpRequestOptions;

import com.google.common.collect.Multimap;

/**
 * Options used to control paginated results (aka list commands).
 */
public class PaginationOptions extends BaseHttpRequestOptions {
    private static final int MAX_LIMIT = 10000;

    public PaginationOptions queryParameters(Multimap<String, String> queryParams) {
        checkNotNull(queryParams, "queryParams");
        queryParameters.putAll(queryParams);
        return this;
    }

    /**
     * Only return objects changed since a specified time.
     *
     * @deprecated The {@code changes-since} query does not apply to all OpenStack APIs.
     * Please refer to the OpenStack Nova {@code ListOptions.changesSince(Date)} and Glance
     * {@code ListImageOptions.changesSince(Date)}. To be removed in jclouds 2.0.
     */
    @Deprecated
    public PaginationOptions changesSince(Date changesSince) {
        String timestamp = Long.toString(checkNotNull(changesSince, "changesSince").getTime() / 1000);
        queryParameters.put("changes-since", timestamp);
        return this;
    }

    public PaginationOptions marker(String marker) {
        queryParameters.put("marker", checkNotNull(marker, "marker"));
        return this;
    }

    public PaginationOptions limit(int limit) {
        checkState(limit >= 0, "limit must be >= 0");
        checkState(limit <= MAX_LIMIT, "limit must be <= " + MAX_LIMIT);
        queryParameters.put("limit", Integer.toString(limit));
        return this;
    }

    public static class Builder {
        public static PaginationOptions queryParameters(Multimap<String, String> queryParams) {
            return new PaginationOptions().queryParameters(queryParams);
        }

        /**
         * @see PaginationOptions#changesSince(Date)
         * @deprecated The {@code changes-since} query does not apply to all OpenStack APIs.
         * Please refer to the OpenStack Nova {@code ListOptions.changesSince(Date)} and Glance
         * {@code ListImageOptions.changesSince(Date)}. To be removed in jclouds 2.0.
         */
        @Deprecated
        public static PaginationOptions changesSince(Date changesSince) {
            return new PaginationOptions().changesSince(changesSince);
        }

        public static PaginationOptions marker(String marker) {
            return new PaginationOptions().marker(marker);
        }

        public static PaginationOptions limit(int limit) {
            return new PaginationOptions().limit(limit);
        }
    }
}
