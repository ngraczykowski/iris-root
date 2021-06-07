package com.silenteight.hsbc.bridge.amqp;

import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!dev")
class ListenerConfiguration {

  @Bean
  NewGovernanceModelListener newGovernanceModelListener(
      GovernanceModelManager governanceModelManager) {
    return new NewGovernanceModelListener(governanceModelManager);
  }

  @Bean
  NewWorldCheckModelListener newWorldCheckModelListener(
      WorldCheckModelManager worldCheckModelManager) {
    return new NewWorldCheckModelListener(worldCheckModelManager);
  }

  @Bean
  WorldCheckModelTransferStatusListener worldCheckModelTransferStatusListener(
      WorldCheckModelManager worldCheckModelManager) {
    return new WorldCheckModelTransferStatusListener(worldCheckModelManager);
  }

  @Bean
  RecommendationGeneratedListener recommendationGeneratedListener(
      ApplicationEventPublisher eventPublisher) {
    return new RecommendationGeneratedListener(eventPublisher);
  }
}
