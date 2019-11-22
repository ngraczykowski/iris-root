package com.silenteight.sens.webapp.users.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.kernel.security.authority.Role;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RoleToAuthoritiesMapper {

  public static Set<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
    Stream<GrantedAuthority> authorities = roles
        .stream()
        .flatMap(role -> role.getGrantedAuthorities().stream());

    Stream<GrantedAuthority> rolesAuthorities = roles
        .stream()
        .map(GrantedAuthority.class::cast);

    return Stream.concat(authorities, rolesAuthorities).collect(toSet());
  }
}
