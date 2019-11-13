package com.silenteight.sens.webapp.backend.security.dto;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class PrincipalDtoMapper {

  public PrincipalDto map(@NonNull SensUserDetails user) {
    List<String> authorities = getAuthorities(user);
    return PrincipalDto
        .builder()
        .userName(user.getUsername())
        .authorities(authorities)
        .displayName(user.getDisplayName())
        .superUser(user.isSuperUser())
        .build();
  }

  private static List<String> getAuthorities(SensUserDetails user) {
    return user.getAuthorities().stream()
               .map(GrantedAuthority::getAuthority)
               .collect(Collectors.toList());
  }
}
