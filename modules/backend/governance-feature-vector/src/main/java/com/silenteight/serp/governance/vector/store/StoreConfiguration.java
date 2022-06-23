package com.silenteight.serp.governance.vector.store;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.vector.domain.FeatureVectorService;
import com.silenteight.serp.governance.vector.usage.domain.UsageService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StoreConfiguration {

  @Bean
  StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase(
      UsageService usageService,
      FeatureVectorService featureVectorService,
      CanonicalFeatureVectorFactory canonicalFeatureVectorFactory) {

    return new StoreFeatureVectorSolvedUseCase(
        featureVectorService, usageService, canonicalFeatureVectorFactory);
  }
}
