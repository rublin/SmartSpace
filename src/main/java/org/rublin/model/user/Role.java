package org.rublin.model.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Ruslan Sheremet (rublin) on 04.09.2016.
 */
public enum  Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_ADMIN;


    @Override
    public String getAuthority() {
        return name();
    }
}
