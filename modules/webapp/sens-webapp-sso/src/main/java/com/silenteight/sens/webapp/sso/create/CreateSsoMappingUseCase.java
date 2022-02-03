package com.silenteight.sens.webapp.sso.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.sso.domain.SsoMappingService;

@RequiredArgsConstructor
class CreateSsoMappingUseCase {

  @NonNull
  private final SsoMappingService ssoMappingService;

  void activate(CreateSsoMappingCommand command) {
    ssoMappingService.create(command);
  }
}
