package com.silenteight.sens.webapp.kernel.security;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static java.util.Collections.emptySet;

@Builder
@Value
public class SensUserDetails implements UserDetails {

    private static final long serialVersionUID = -1405182130131106574L;

    private Long userId;

    @NonNull
    private String username;

    private String password;

    private boolean superUser;

    private String displayName;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = true;

    @NonNull
    @Builder.Default
    private Set<GrantedAuthority> authorities = emptySet();

    public boolean hasAnyAuthorities() {
        return !getAuthorities().isEmpty();
    }
}
