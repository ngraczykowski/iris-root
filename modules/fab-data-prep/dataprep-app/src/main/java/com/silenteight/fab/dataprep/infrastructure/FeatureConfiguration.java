package com.silenteight.fab.dataprep.infrastructure;

import com.silenteight.fab.dataprep.domain.feature.FabFeature;
import com.silenteight.fab.dataprep.domain.feature.GenderFeature;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class FeatureConfiguration {

  @Bean
  @ConditionalOnProperty("feeding.features.gender-feature.enabled")
  FabFeature genderFeature() {
    return new GenderFeature();
  }
}
