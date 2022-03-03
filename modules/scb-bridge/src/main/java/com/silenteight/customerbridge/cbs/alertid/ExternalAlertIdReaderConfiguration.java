package com.silenteight.customerbridge.cbs.alertid;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.cbs.batch.ScbBridgeConfigProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@RequiredArgsConstructor
class ExternalAlertIdReaderConfiguration {

  private final JdbcTemplate jdbcTemplate;
  private final AlertIdPublisher alertIdPublisher;
  private final ScbBridgeConfigProperties configProperties;

  @Bean
  ExternalAlertIdReader externalAlertIdReader() {
    return new ExternalAlertIdReader(
        chunkProcessor(), jdbcTemplate, configProperties.getQueryTimeout());
  }

  private ChunkProcessor chunkProcessor() {
    return new ChunkProcessor(alertIdPublisher);
  }
}
