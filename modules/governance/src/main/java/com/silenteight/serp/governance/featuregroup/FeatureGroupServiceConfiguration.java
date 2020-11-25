package com.silenteight.serp.governance.featuregroup;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class FeatureGroupServiceConfiguration {

  private final FeatureVectorService featureVectorService;
  private final DecisionTreeFacade decisionTreeFacade;

  @Bean
  FeatureGroupService featureGroupService() {
    return new FeatureGroupService(featureVectorService, decisionTreeFacade);
  }
}
