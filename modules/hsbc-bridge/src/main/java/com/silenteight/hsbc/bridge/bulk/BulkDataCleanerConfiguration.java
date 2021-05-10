package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.retention.DataCleaner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkDataCleanerConfiguration {

  private final BulkPayloadRepository bulkPayloadRepository;

  @Bean
  DataCleaner bulkDataCleaner() {
    return new BulkDataCleaner(bulkPayloadRepository);
  }
}
