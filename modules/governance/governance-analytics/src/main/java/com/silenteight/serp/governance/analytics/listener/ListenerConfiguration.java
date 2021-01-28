package com.silenteight.serp.governance.analytics.listener;

import com.silenteight.serp.governance.analytics.StoreFeatureVectorSolvedUseCase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ListenerConfiguration {

  @Bean
  FeatureVectorSolvedIntegrationFlowAdapter featureVectorSolvedIntegrationFlowAdapter(
      StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase) {
    FeatureVectorSolvedMessageHandler handler =
        new FeatureVectorSolvedMessageHandler(storeFeatureVectorSolvedUseCase);
    return new FeatureVectorSolvedIntegrationFlowAdapter(handler);
  }
}
