
package org.jclouds.s3.domain;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class AccessControlList {

    private CanonicalUser owner;
    private final List<Grant> grants = Lists.newArrayList();

    public void setOwner(CanonicalUser owner) {
        this.owner = owner;
    }

    public CanonicalUser getOwner() {
        return owner;
    }

    public List<Grant> getGrants() {
        return Collections.unmodifiableList(grants);
    }

    public Set<Grantee> getGrantees() {
        return grants.stream()
                .map(Grant::getGrantee)
                .collect(Collectors.toUnmodifiableSet());
    }

    public AccessControlList addPermission(Grantee grantee, String permission) {
        grants.add(new Grant(grantee, permission));
        return this;
    }

    public AccessControlList addPermission(URI groupGranteeURI, String permission) {
        return addPermission(new GroupGrantee(groupGranteeURI), permission);
    }

    public AccessControlList revokePermission(Grantee grantee, String permission) {
        grants.removeIf(grant -> grant.getGrantee().equals(grantee) && grant.getPermission().equals(permission));
        return this;
    }

    public AccessControlList revokePermission(URI groupGranteeURI, String permission) {
        return revokePermission(new GroupGrantee(groupGranteeURI), permission);
    }

    public AccessControlList revokeAllPermissions(Grantee grantee) {
        grants.removeAll(findGrantsForGrantee(grantee.getIdentifier()));
        return this;
    }

    public Collection<String> getPermissions(String granteeId) {
        return findGrantsForGrantee(granteeId).stream()
                .map(Grant::getPermission)
                .collect(Collectors.toList());
    }

    public Collection<String> getPermissions(Grantee grantee) {
        return getPermissions(grantee.getIdentifier());
    }

    public Collection<String> getPermissions(URI granteeURI) {
        return getPermissions(granteeURI.toASCIIString());
    }

    public boolean hasPermission(String granteeId, String permission) {
        return getPermissions(granteeId).contains(permission);
    }

    public boolean hasPermission(Grantee grantee, String permission) {
        return hasPermission(grantee.getIdentifier(), permission);
    }

    public boolean hasPermission(URI granteeURI, String permission) {
        return getPermissions(granteeURI).contains(permission);
    }

    protected Collection<Grant> findGrantsForGrantee(final String granteeId) {
        return grants.stream()
                .filter(grant -> grant.getGrantee().getIdentifier().equals(granteeId))
                .collect(Collectors.toList());
    }

    public static AccessControlList fromCannedAccessPolicy(CannedAccessPolicy cannedAP, String ownerId) {
        AccessControlList acl = new AccessControlList();
        acl.setOwner(new CanonicalUser(ownerId));
        acl.addPermission(new CanonicalUserGrantee(ownerId), Permission.FULL_CONTROL);

        switch (cannedAP) {
            case AUTHENTICATED_READ:
                acl.addPermission(GroupGranteeURI.AUTHENTICATED_USERS, Permission.READ);
                break;
            case PUBLIC_READ:
                acl.addPermission(GroupGranteeURI.ALL_USERS, Permission.READ);
                break;
            case PUBLIC_READ_WRITE:
                acl.addPermission(GroupGranteeURI.ALL_USERS, Permission.READ);
                acl.addPermission(GroupGranteeURI.ALL_USERS, Permission.WRITE);
                break;
            case PRIVATE:
            default:
                break;
        }

        return acl;
    }

    public static final class Permission {
        public static final String READ = "READ";
        public static final String WRITE = "WRITE";
        public static final String READ_ACP = "READ_ACP";
        public static final String WRITE_ACP = "WRITE_ACP";
        public static final String FULL_CONTROL = "FULL_CONTROL";

        private Permission() {
            throw new AssertionError("intentionally unimplemented");
        }
    }

    public static class Grant implements Comparable<Grant> {

        private Grantee grantee;
        private final String permission;

        public Grant(Grantee grantee, String permission) {
            this.grantee = grantee;
            this.permission = permission;
        }

        public Grantee getGrantee() {
            return grantee;
        }

        @VisibleForTesting
        public void setGrantee(Grantee grantee) {
            this.grantee = grantee;
        }

        public String getPermission() {
            return permission;
        }

        @Override
        public String toString() {
            return "Grant{" +
                    "grantee=" + grantee +
                    ", permission='" + permission + '\'' +
                    '}';
        }

        public int compareTo(Grant o) {
            return (this == o) ? 0 : (grantee.getIdentifier() + "\n" + permission)
                    .compareTo(o.grantee.getIdentifier() + "\n" + o.permission);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((grantee == null) ? 0 : grantee.hashCode());
            result = prime * result + ((permission == null) ? 0 : permission.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Grant other = (Grant) obj;
            return (grantee != null ? grantee.equals(other.grantee) : other.grantee == null) &&
                   (permission != null ? permission.equals(other.permission) : other.permission == null);
        }
    }

    public abstract static class Grantee implements Comparable<Grantee> {
        private final String identifier;

        protected Grantee(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }

        @Override
        public String toString() {
            return "Grantee{" +
                    "identifier='" + identifier + '\'' +
                    '}';
        }

        public int compareTo(Grantee o) {
            return (this == o) ? 0 : getIdentifier().compareTo(o.getIdentifier());
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Grantee other = (Grantee) obj;
            return (identifier != null ? identifier.equals(other.identifier) : other.identifier == null);
        }
    }

    public static class EmailAddressGrantee extends Grantee {
        public EmailAddressGrantee(String emailAddress) {
            super(emailAddress);
        }

        public String getEmailAddress() {
            return getIdentifier();
        }
    }

    public static class CanonicalUserGrantee extends Grantee {
        private final String displayName;

        public CanonicalUserGrantee(String id, String displayName) {
            super(id);
            this.displayName = displayName;
        }

        public CanonicalUserGrantee(String id) {
            this(id, null);
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return "CanonicalUserGrantee{" +
                    "displayName='" + displayName + '\'' +
                    ", identifier='" + getIdentifier() + '\'' +
                    '}';
        }
    }

    public static final class GroupGranteeURI {
        public static final URI ALL_USERS = URI.create("http://acs.amazonaws.com/groups/global/AllUsers");
        public static final URI AUTHENTICATED_USERS = URI.create("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");
        public static final URI LOG_DELIVERY = URI.create("http://acs.amazonaws.com/groups/s3/LogDelivery");

        private GroupGranteeURI() {
            throw new AssertionError("intentionally unimplemented");
        }
    }

    public static class GroupGrantee extends Grantee {

        public GroupGrantee(URI groupURI) {
            super(groupURI.toASCIIString());
        }
    }

    @Override
    public String toString() {
        return "AccessControlList{" +
                "owner=" + owner +
                ", grants=" + grants +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((grants == null) ? 0 : grants.hashCode());
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AccessControlList other = (AccessControlList) obj;
        return (grants != null ? grants.equals(other.grants) : other.grants == null) &&
               (owner != null ? owner.equals(other.owner) : other.owner == null);
    }
}
