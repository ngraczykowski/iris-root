package com.silenteight.sens.webapp.kernel.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

public interface WebappUserDetails extends UserDetails {

  String getDisplayName();

  default boolean hasNoAuthorities() {
    return getAuthorities().isEmpty();
  }

  default Set<String> getAuthorityNames() {
    return getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toSet());
  }
}
