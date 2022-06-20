/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.Validation;

@Configuration
@RequiredArgsConstructor
class GnsRtRequestGeneratorConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  GnsRtRequestGenerator gnsRtRequestGenerator() {
    return new OracleDatabaseGnsRtRequestGenerator(
        jdbcTemplate,
        getGnsRtRequestMapper(),
        getGnsRtRecommendationRequestValidator());
  }

  private static GnsRtRequestMapper getGnsRtRequestMapper() {
    return new GnsRtRequestMapper(new HitDetailsParser());
  }

  private static GnsRtRecommendationRequestValidator getGnsRtRecommendationRequestValidator() {
    return new GnsRtRecommendationRequestValidator(
        Validation.buildDefaultValidatorFactory().getValidator()
    );
  }
}
