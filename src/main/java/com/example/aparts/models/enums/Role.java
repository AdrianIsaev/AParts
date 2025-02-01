package com.example.aparts.models.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER, ROLE_EMPLOYEE, ROLE_DELIVERYMAN;
    @Override
    public String getAuthority() {
        return name();
    }
}
