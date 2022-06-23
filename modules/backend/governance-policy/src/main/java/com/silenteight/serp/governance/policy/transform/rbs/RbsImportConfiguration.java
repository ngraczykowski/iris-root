package com.silenteight.serp.governance.policy.transform.rbs;

import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(RbsImportProperties.class)
class RbsImportConfiguration {

  @Bean
  RbsImportUseCase rbsImportUseCase(
      RbsParser rbsParser,
      RbsToPolicyTransformationService transformationService) {

    return new RbsImportUseCase(rbsParser, transformationService);
  }

  @Bean
  RbsToPolicyTransformationService transformationService(
      PolicyService policyService,
      PolicyStepsRequestQuery policyStepsRequestQuery) {

    return new RbsToPolicyTransformationService(policyService, policyStepsRequestQuery);
  }

  @Bean
  RbsParser rbsParser(@Valid RbsImportProperties rbsImportProperties) {

    return new RbsParser(rbsImportProperties);
  }
}
