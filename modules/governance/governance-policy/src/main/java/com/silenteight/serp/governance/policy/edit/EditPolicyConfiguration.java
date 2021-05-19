package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;

import com.silenteight.serp.governance.policy.domain.PolicyService;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EditPolicyConfiguration {

  @Bean
  EditPolicyUseCase editPolicyUseCase(@NonNull PolicyService policyService) {
    return new EditPolicyUseCase(policyService);
  }

  @Bean
  SavePolicyUseCase savePolicyUseCase(@NonNull PolicyService policyService) {
    return new SavePolicyUseCase(policyService);
  }

  @Bean
  UsePolicyUseCase usePolicyUseCase(
      @NonNull PolicyService policyService, @NonNull ApplicationEventPublisher eventPublisher) {
    return new UsePolicyUseCase(policyService, eventPublisher);
  }

  @Bean
  ArchivePolicyUseCase archivePolicyUseCase(@NonNull PolicyService policyService) {
    return new ArchivePolicyUseCase(policyService);
  }
}
