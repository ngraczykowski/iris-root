package com.silenteight.payments.bridge.datasource.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.port.outgoing.FeatureMapper;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@RequiredArgsConstructor
@Configuration
class FeatureMapperFactoryConfiguration {

  private final ObjectProvider<FeatureMapper> featureMappers;

  @Bean
  FeatureMapperFactory featureMapperFactory() {
    return new FeatureMapperFactory(featureMappers.stream().collect(
        Collectors.toMap(FeatureMapper::getType, identity())));
  }
}
