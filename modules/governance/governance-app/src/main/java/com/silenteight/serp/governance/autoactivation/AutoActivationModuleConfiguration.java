package com.silenteight.serp.governance.autoactivation;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
@RequiredArgsConstructor
class AutoActivationModuleConfiguration {

  @Bean
  ActivateDecisionGroupForDefaultDecisionTreeUseCase activateDecisionGroupForDefaultDecisionTree(
      DecisionTreeFacade decisionTreeFacade,
      ActivationService activationService) {

    return new ActivateDecisionGroupForDefaultDecisionTreeUseCase(
        decisionTreeFacade, activationService);
  }

  @Bean
  AutoActivationFacade autoActivationFacade(
      ActivateDecisionGroupForDefaultDecisionTreeUseCase
          activateDecisionGroupForDefaultDecisionTreeUseCase
  ) {
    return new AutoActivationFacade(activateDecisionGroupForDefaultDecisionTreeUseCase);
  }
}
