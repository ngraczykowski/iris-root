package com.silenteight.adjudication.engine.app.scheduling;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.DeleteMatchFeatureValuesUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(DeleteMatchFeatureValueProperties.class)
class DeleteMatchFeatureValueConfiguration {

  private final DeleteMatchFeatureValuesUseCase deleteMatchFeatureValuesUseCase;

  @Valid
  private final DeleteMatchFeatureValueProperties properties;

  @Bean
  DeleteMatchFeatureValueJob deleteMatchFeatureValueJob() {
    return new DeleteMatchFeatureValueJob(
        deleteMatchFeatureValuesUseCase, properties.getFeatures());
  }
}
