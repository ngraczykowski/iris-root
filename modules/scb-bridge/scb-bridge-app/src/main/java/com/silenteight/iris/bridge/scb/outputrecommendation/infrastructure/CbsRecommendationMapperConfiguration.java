/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.infrastructure;

import com.silenteight.iris.bridge.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing.CbsRecommendationMapper;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CbsRecommendationMapperConfigurationProperties.class)
public class CbsRecommendationMapperConfiguration {

  @Bean
  CbsRecommendationMapper cbsRecommendationMapper(
      PayloadConverter payloadConverter,
      CbsRecommendationProperties cbsRecommendationProperties,
      CbsRecommendationMapperConfigurationProperties mapperConfigurationProperties) {
    return new CbsRecommendationMapper(
        payloadConverter,
        cbsRecommendationProperties,
        mapperConfigurationProperties);
  }
}
