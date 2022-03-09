package com.silenteight.sens.webapp.sso.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.sso.domain.SsoMappingService;
import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

@RequiredArgsConstructor
class CreateSsoMappingUseCase {

  @NonNull
  private final SsoMappingService ssoMappingService;

  SsoMappingDto activate(CreateSsoMappingCommand command) {
    return ssoMappingService.create(command);
  }
}
