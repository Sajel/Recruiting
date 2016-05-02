package ua.kpi.nc.persistence.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Chalienko on 15.04.2016.
 */
public interface Role extends Serializable, GrantedAuthority {
    Long getId();

    void setId(Long id);

    String getRoleName();

    void setRoleName(String roleName);

    Set<User> getUsers();

    void setUsers(Set<User> users);
}
