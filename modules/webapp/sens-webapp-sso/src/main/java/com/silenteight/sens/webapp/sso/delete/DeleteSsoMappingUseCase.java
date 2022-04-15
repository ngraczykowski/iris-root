package com.silenteight.sens.webapp.sso.delete;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.sso.domain.SsoMappingService;

@AllArgsConstructor
class DeleteSsoMappingUseCase {

  @NonNull
  private final SsoMappingService ssoMappingService;

  void activate(DeleteSsoMappingRequest command) {
    ssoMappingService.deleteSsoMapping(command);
  }
}
