package com.silenteight.scb.outputrecommendation.infrastructure;

import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.outputrecommendation.adapter.outgoing.CbsRecommendationMapper;

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
