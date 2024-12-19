
package org.jclouds.openstack.v2_0.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.any;
import static org.jclouds.openstack.v2_0.predicates.ExtensionPredicates.aliasEquals;
import static org.jclouds.openstack.v2_0.predicates.ExtensionPredicates.nameEquals;
import static org.jclouds.openstack.v2_0.predicates.ExtensionPredicates.namespaceOrAliasEquals;
import static org.jclouds.util.Optionals2.unwrapIfOptional;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.inject.Inject;

import org.jclouds.openstack.keystone.v2_0.config.NamespaceAliases;
import org.jclouds.openstack.v2_0.domain.Extension;
import org.jclouds.reflect.InvocationSuccess;
import org.jclouds.rest.functions.ImplicitOptionalConverter;

import com.google.common.base.Optional;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class PresentWhenExtensionAnnotationMatchesExtensionSet implements ImplicitOptionalConverter {
    private final LoadingCache<String, Set<? extends Extension>> extensions;
    private final Map<URI, Set<URI>> aliases;

    @Inject
    PresentWhenExtensionAnnotationMatchesExtensionSet(
            LoadingCache<String, Set<? extends Extension>> extensions, @NamespaceAliases Map<URI, Set<URI>> aliases) {
        this.extensions = extensions;
        this.aliases = aliases == null ? ImmutableMap.<URI, Set<URI>>of() : ImmutableMap.copyOf(aliases);
    }

    private boolean checkExtension(String invocationArg, URI namespace, Set<URI> aliasesForNamespace, String alias, String name) {
        return isNamespaceOrAliasMatch(invocationArg, namespace, aliasesForNamespace) ||
               isAliasMatch(invocationArg, alias) ||
               isNameMatch(invocationArg, name);
    }

    private boolean isNamespaceOrAliasMatch(String invocationArg, URI namespace, Set<URI> aliasesForNamespace) {
        return any(extensions.getUnchecked(invocationArg), namespaceOrAliasEquals(namespace, aliasesForNamespace));
    }

    private boolean isAliasMatch(String invocationArg, String alias) {
        return !"".equals(alias) && any(extensions.getUnchecked(invocationArg), aliasEquals(alias));
    }

    private boolean isNameMatch(String invocationArg, String name) {
        return !"".equals(name) && any(extensions.getUnchecked(invocationArg), nameEquals(name));
    }

    @Override
    public Optional<Object> apply(InvocationSuccess input) {
        Class<?> target = unwrapIfOptional(input.getInvocation().getInvokable().getReturnType());
        Optional<org.jclouds.openstack.v2_0.services.Extension> ext = Optional.fromNullable(target
                .getAnnotation(org.jclouds.openstack.v2_0.services.Extension.class));
        if (ext.isPresent()) {
            URI namespace = URI.create(ext.get().namespace());
            List<Object> args = input.getInvocation().getArgs();
            Set<URI> aliasesForNamespace = aliases.getOrDefault(namespace, Sets.<URI>newHashSet());
            String name = ext.get().name();
            String alias = ext.get().alias();

            if (args.isEmpty() || (args.size() == 1 && checkExtension(checkNotNull(args.get(0), "arg[0] in %s", input).toString(), namespace, aliasesForNamespace, alias, name))) {
                return input.getResult();
            }

            return Optional.absent();
        } else {
            return input.getResult();
        }
    }

    @Override
    public String toString() {
        return "PresentWhenExtensionAnnotationMatchesExtensionSet()";
    }
}
