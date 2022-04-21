package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import com.silenteight.connector.ftcc.ingest.domain.BatchIdGenerator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RestConfiguration {

  @Bean
  BatchIdGenerator batchIdGenerator() {
    return new BatchIdGenerator();
  }
}
