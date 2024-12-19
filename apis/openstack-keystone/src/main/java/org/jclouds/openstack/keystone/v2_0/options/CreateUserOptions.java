
package org.jclouds.openstack.keystone.v2_0.options;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableMap;

public class CreateUserOptions implements MapBinder {
    @Inject
    private BindToJsonPayload jsonBinder;

    private String tenant;
    private String password;
    private String email;
    private boolean enabled;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof CreateUserOptions) {
            final CreateUserOptions other = CreateUserOptions.class.cast(object);
            return equal(tenant, other.tenant) && equal(password, other.password) && equal(email, other.email)
                    && equal(enabled, other.enabled);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tenant, password, email, enabled);
    }

    protected ToStringHelper string() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("tenant", tenant)
                .add("password", password)
                .add("email", email)
                .add("enabled", enabled);
    }

    @Override
    public String toString() {
        return string().toString();
    }

    static class ServerRequest {
        final String name;
        String tenantId;
        String password;
        String email;
        boolean enabled;

        private ServerRequest(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
        ServerRequest user = new ServerRequest(
                checkNotNull(postParams.get("name"), "name parameter not present").toString(),
                checkNotNull(postParams.get("password"), "password parameter not present").toString());
        populateServerRequest(user, postParams);
        return bindToRequest(request, (Object) ImmutableMap.of("user", user));
    }

    private void populateServerRequest(ServerRequest user, Map<String, Object> postParams) {
        if (email != null) user.email = email;
        if (password != null) user.password = password;
        if (tenant != null) user.tenantId = tenant;
        user.enabled = enabled;
    }

    public String getTenant() {
        return this.tenant;
    }

    public CreateUserOptions tenant(String tenant) {
        this.tenant = tenant;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public CreateUserOptions email(String email) {
        this.email = email;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public CreateUserOptions enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public static class Builder {
        public static CreateUserOptions tenant(String tenant) {
            CreateUserOptions options = new CreateUserOptions();
            return options.tenant(tenant);
        }

        public static CreateUserOptions email(String email) {
            CreateUserOptions options = new CreateUserOptions();
            return options.email(email);
        }

        public static CreateUserOptions enabled(boolean enabled) {
            CreateUserOptions options = new CreateUserOptions();
            return options.enabled(enabled);
        }
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object input) {
        return jsonBinder.bindToRequest(request, input);
    }
}
