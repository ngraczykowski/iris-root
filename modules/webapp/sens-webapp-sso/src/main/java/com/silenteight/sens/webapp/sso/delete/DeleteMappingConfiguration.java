package com.silenteight.sens.webapp.sso.delete;

import com.silenteight.sens.webapp.sso.domain.SsoMappingService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DeleteMappingConfiguration {

  @Bean
  DeleteSsoMappingUseCase deleteSsoMappingUseCase(SsoMappingService ssoMappingService) {
    return new DeleteSsoMappingUseCase(ssoMappingService);
  }
}
