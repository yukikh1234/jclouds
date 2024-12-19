
package org.jclouds.openstack.keystone.v2_0.functions.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.beans.ConstructorProperties;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import org.jclouds.collect.IterableWithMarker;
import org.jclouds.collect.internal.Arg0ToPagedIterable;
import org.jclouds.http.functions.ParseJson;
import org.jclouds.json.Json;
import org.jclouds.openstack.keystone.v2_0.KeystoneApi;
import org.jclouds.openstack.keystone.v2_0.domain.Service;
import org.jclouds.openstack.keystone.v2_0.extensions.ServiceAdminApi;
import org.jclouds.openstack.v2_0.domain.Link;
import org.jclouds.openstack.v2_0.domain.PaginatedCollection;
import org.jclouds.openstack.v2_0.options.PaginationOptions;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.inject.TypeLiteral;

@Beta
@Singleton
public class ParseServices extends ParseJson<ParseServices.Services> {
    
    static class Services extends PaginatedCollection<Service> {

        @ConstructorProperties({ "OS-KSADM:services", "services_links" })
        protected Services(Iterable<Service> services, Iterable<Link> services_links) {
            super(services, services_links);
        }
    }

    @Inject
    public ParseServices(Json json) {
        super(json, TypeLiteral.get(Services.class));
    }

    public static class ToPagedIterable extends Arg0ToPagedIterable.FromCaller<Service, ToPagedIterable> {

        private final KeystoneApi api;

        @Inject
        protected ToPagedIterable(KeystoneApi api) {
            this.api = checkNotNull(api, "api");
        }

        @Override
        protected Function<Object, IterableWithMarker<Service>> markerToNextForArg0(Optional<Object> ignored) {
            final ServiceAdminApi serviceApi = api.getServiceAdminApi().get();
            return new ListServicesFunction(serviceApi);
        }

        private static class ListServicesFunction implements Function<Object, IterableWithMarker<Service>> {

            private final ServiceAdminApi serviceApi;

            ListServicesFunction(ServiceAdminApi serviceApi) {
                this.serviceApi = serviceApi;
            }

            @SuppressWarnings("unchecked")
            @Override
            public IterableWithMarker<Service> apply(Object input) {
                PaginationOptions paginationOptions = PaginationOptions.class.cast(input);
                return IterableWithMarker.class.cast(serviceApi.list(paginationOptions));
            }

            @Override
            public String toString() {
                return "listServices()";
            }
        }
    }
}
