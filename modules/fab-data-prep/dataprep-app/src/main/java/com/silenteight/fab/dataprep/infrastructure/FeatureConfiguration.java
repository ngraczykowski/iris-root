package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.feature.FabFeature;
import com.silenteight.fab.dataprep.domain.feature.GenderFeature;
import com.silenteight.universaldatasource.api.library.gender.v1.GenderFeatureInputOut;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureConfiguration {

  @Bean
  @ConditionalOnProperty("feeding.features.gender-feature.enabled")
  FabFeature<GenderFeatureInputOut> genderFeature() {
    return new GenderFeature();
  }
}
