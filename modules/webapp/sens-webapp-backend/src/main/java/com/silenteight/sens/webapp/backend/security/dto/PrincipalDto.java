package com.silenteight.sens.webapp.backend.security.dto;

import lombok.Getter;
import lombok.NonNull;

import com.silenteight.sens.webapp.kernel.security.WebappUserDetails;

import java.util.Set;

@Getter
public class PrincipalDto {

  @NonNull
  private final String userName;
  @NonNull
  private final Set<String> authorities;

  private final String displayName;

  public PrincipalDto(WebappUserDetails webappUserDetails) {
    userName = webappUserDetails.getUsername();
    authorities = webappUserDetails.getAuthorityNames();
    displayName = webappUserDetails.getDisplayName();
  }
}
