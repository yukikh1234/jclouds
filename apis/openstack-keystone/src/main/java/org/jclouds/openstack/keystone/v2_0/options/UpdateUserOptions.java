
package org.jclouds.openstack.keystone.v2_0.options;

import static com.google.common.base.Objects.equal;

import java.util.Map;

import jakarta.inject.Inject;

import org.jclouds.http.HttpRequest;
import org.jclouds.rest.MapBinder;
import org.jclouds.rest.binders.BindToJsonPayload;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.ImmutableMap;

public class UpdateUserOptions implements MapBinder {
    @Inject
    private BindToJsonPayload jsonBinder;

    private String name;
    private String email;
    private String password;
    private boolean enabled;

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof UpdateUserOptions) {
            final UpdateUserOptions other = UpdateUserOptions.class.cast(object);
            return equal(name, other.name) && equal(email, other.email)
                    && equal(enabled, other.enabled) && equal(password, other.password);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, email, enabled, password);
    }

    protected ToStringHelper string() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("name", name)
                .add("email", email)
                .add("password", password)
                .add("enabled", enabled);
    }

    @Override
    public String toString() {
        return string().toString();
    }

    static class ServerRequest {
        String name;
        String email;
        String password;
        boolean enabled;
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Map<String, Object> postParams) {
        ServerRequest user = createUserRequest();
        return bindToRequest(request, (Object) ImmutableMap.of("user", user));
    }

    private ServerRequest createUserRequest() {
        ServerRequest user = new ServerRequest();
        user.email = email;
        user.name = name;
        user.password = password;
        user.enabled = enabled;
        return user;
    }

    public String getName() {
        return this.name;
    }

    public UpdateUserOptions name(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UpdateUserOptions password(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public UpdateUserOptions email(String email) {
        this.email = email;
        return this;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public UpdateUserOptions enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public static class Builder {
        public static UpdateUserOptions name(String name) {
            return new UpdateUserOptions().name(name);
        }

        public static UpdateUserOptions email(String email) {
            return new UpdateUserOptions().email(email);
        }

        public static UpdateUserOptions enabled(boolean enabled) {
            return new UpdateUserOptions().enabled(enabled);
        }

        public static UpdateUserOptions password(String password) {
            return new UpdateUserOptions().password(password);
        }
    }

    @Override
    public <R extends HttpRequest> R bindToRequest(R request, Object input) {
        return jsonBinder.bindToRequest(request, input);
    }
}
