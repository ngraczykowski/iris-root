package com.silenteight.sens.webapp.security.acl;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class UserAuthoritySidRetrievalStrategy implements SidRetrievalStrategy {

  private static final String ROLE_AUTHORITY_PREFIX = "ROLE_";

  @Override
  public List<Sid> getSids(Authentication authentication) {
    if (authentication.getPrincipal() instanceof SensUserDetails) {
      SensUserDetails userDetails = (SensUserDetails) authentication.getPrincipal();
      return getSids(userDetails.getUserId(), authentication.getAuthorities());
    } else {
      return emptyList();
    }
  }

  private static List<Sid> getSids(
      long userId, Collection<? extends GrantedAuthority> authorities) {
    
    return authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .filter(a -> a.startsWith(ROLE_AUTHORITY_PREFIX))
        .map(a -> new UserAuthoritySid(userId, a))
        .collect(toList());
  }
}
