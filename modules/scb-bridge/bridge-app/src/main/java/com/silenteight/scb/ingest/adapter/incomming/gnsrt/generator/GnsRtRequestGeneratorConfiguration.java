package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
class GnsRtRequestGeneratorConfiguration {

  private final JdbcTemplate jdbcTemplate;

  @Bean
  GnsRtRequestGenerator gnsRtRequestGenerator() {
    return new OracleDatabaseGnsRtRequestGenerator(jdbcTemplate, getGnsRtRequestMapper());
  }

  private static GnsRtRequestMapper getGnsRtRequestMapper() {
    return new GnsRtRequestMapper(new HitDetailsParser());
  }
}