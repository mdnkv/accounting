package dev.mednikov.accounting.shared.bootstrap;

import java.util.List;

final class RoleAuthorityDto {

    private String name;
    private List<String> authorities;

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
