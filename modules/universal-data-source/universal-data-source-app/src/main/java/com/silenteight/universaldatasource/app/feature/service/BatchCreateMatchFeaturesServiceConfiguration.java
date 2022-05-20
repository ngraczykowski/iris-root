package com.silenteight.universaldatasource.app.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureMapper;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@RequiredArgsConstructor
@Configuration
class BatchCreateMatchFeaturesServiceConfiguration {

  private final MessageRegistry messageRegistry;
  private final FeatureDataAccess dataAccess;
  private final ObjectProvider<FeatureMapper> featureMappers;

  @Bean
  BatchCreateMatchFeaturesService batchCreateMatchFeaturesService() {
    return new BatchCreateMatchFeaturesService(
        messageRegistry, dataAccess, featureMappers.stream().collect(
        Collectors.toMap(FeatureMapper::getType, identity())));
  }

}
