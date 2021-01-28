package com.silenteight.serp.governance.analytics;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.analytics.usage.UsageService;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AnalyticsConfiguration {

  @Bean
  StoreFeatureVectorSolvedUseCase storeFeatureVectorSolvedUseCase(
      UsageService usageService,
      FeatureVectorService featureVectorService,
      CanonicalFeatureVectorFactory canonicalFeatureVectorFactory) {

    return new StoreFeatureVectorSolvedUseCase(
        featureVectorService, usageService, canonicalFeatureVectorFactory);
  }
}
