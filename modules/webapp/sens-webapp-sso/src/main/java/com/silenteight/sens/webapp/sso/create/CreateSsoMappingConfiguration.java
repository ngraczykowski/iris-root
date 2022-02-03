package com.silenteight.sens.webapp.sso.create;

import com.silenteight.sens.webapp.sso.domain.SsoMappingService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CreateSsoMappingConfiguration {

  @Bean
  CreateSsoMappingUseCase createSsoMappingUseCase(SsoMappingService ssoMappingService) {
    return new CreateSsoMappingUseCase(ssoMappingService);
  }
}
