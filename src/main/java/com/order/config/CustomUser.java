package com.order.config;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
public class CustomUser implements UserDetails {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private int loginCount;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createAt;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isVerified;

    @Builder(builderMethodName = "Anonymous", builderClassName = "Anonymous")
    public CustomUser() {
        this.userId = null;
        this.username = "AnonymouseUser";
        this.authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
    }
}
