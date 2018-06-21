package io.github.yuokada.marathon.plugin.athenz;

import com.google.common.base.Objects;
import com.yahoo.athenz.auth.token.RoleToken;
import java.util.List;
import mesosphere.marathon.plugin.auth.Identity;

public class AthenzIdentity implements Identity {

    private List<String> roles = null;
    private String user;
    private RoleToken roleToken;

    public AthenzIdentity(){
        this.user = "guest";
    }

    public AthenzIdentity(RoleToken token) {
        this.roleToken = token;
        this.user = token.getProxyUser();
        this.roles = token.getRoles();
    }

    public String getUser() {
        return user;
    }

    public RoleToken getRoleToken() {
        return roleToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public Boolean isGuestUser(){
        return user == "guest";
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AthenzIdentity that = (AthenzIdentity) o;
        return Objects.equal(roles, that.roles) &&
                Objects.equal(user, that.user) &&
                Objects.equal(roleToken, that.roleToken);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(roles, user, roleToken);
    }

    @Override
    public String toString()
    {
        return "AthenzIdentity{" +
                "roles=" + roles +
                ", user='" + user + '\'' +
                ", roleToken=" + roleToken +
                '}';
    }
}
