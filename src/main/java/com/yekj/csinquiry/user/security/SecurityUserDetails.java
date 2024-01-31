package com.yekj.csinquiry.user.security;

import com.yekj.csinquiry.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUserDetails implements UserDetails {

    private User user;

    public SecurityUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        if(user.getGroup().getId() == null || user.getEnabled().equals("Y")) return false;
        else return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        if(user.getGroup().getId() == null || user.getEnabled().equals("Y")) return false;
        else return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if(user.getGroup().getId() == null || user.getEnabled().equals("Y")) return false;
        else return true;
    }
}
