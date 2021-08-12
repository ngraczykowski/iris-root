package com.silenteight.hsbc.bridge.amqp;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.BridgeApiProperties;
import com.silenteight.hsbc.bridge.model.transfer.GovernanceModelManager;
import com.silenteight.hsbc.bridge.model.transfer.HistoricalDecisionsModelManager;
import com.silenteight.hsbc.bridge.model.transfer.WorldCheckModelManager;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!dev")
@Configuration
@EnableConfigurationProperties(BridgeApiProperties.class)
@RequiredArgsConstructor
class ListenerConfiguration {

  private final BridgeApiProperties bridgeApiProperties;

  @Bean
  NewGovernanceModelListener newGovernanceModelListener(GovernanceModelManager manager) {
    return new NewGovernanceModelListener(
        bridgeApiProperties.getAddress(), manager);
  }

  @Bean
  NewWorldCheckModelListener newWorldCheckModelListener(
      WorldCheckModelManager worldCheckModelManager) {
    return new NewWorldCheckModelListener(bridgeApiProperties.getAddress(), worldCheckModelManager);
  }

  @Bean
  WorldCheckModelTransferStatusListener worldCheckModelTransferStatusListener(
      WorldCheckModelManager worldCheckModelManager) {
    return new WorldCheckModelTransferStatusListener(worldCheckModelManager);
  }

  @Bean
  NewHistoricalDecisionsModelListener newHistoricalDecisionsModelListener(
      HistoricalDecisionsModelManager historicalDecisionsModelManager) {
    return new NewHistoricalDecisionsModelListener(
        bridgeApiProperties.getAddress(), historicalDecisionsModelManager);
  }

  @Bean
  HistoricalDecisionsModelTransferStatusListener historicalDecisionsModelTransferStatusListener(
      HistoricalDecisionsModelManager historicalDecisionsModelManager) {
    return new HistoricalDecisionsModelTransferStatusListener(historicalDecisionsModelManager);
  }

  @Bean
  RecommendationGeneratedListener recommendationGeneratedListener(
      ApplicationEventPublisher eventPublisher) {
    return new RecommendationGeneratedListener(eventPublisher);
  }
}
