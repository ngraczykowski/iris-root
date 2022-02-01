package com.silenteight.sens.webapp.sso.domain;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.sso.delete.DeleteSsoMappingRequest;
import com.silenteight.sep.usermanagement.api.IdentityProviderRoleMapper;

@Slf4j
@AllArgsConstructor
public class SsoMappingService {

  @NonNull
  private final IdentityProviderRoleMapper identityProviderRoleMapper;

  public void deleteSsoMapping(DeleteSsoMappingRequest request) {
    identityProviderRoleMapper.deleteMapping(request.getSsoMappingId());
  }
}
