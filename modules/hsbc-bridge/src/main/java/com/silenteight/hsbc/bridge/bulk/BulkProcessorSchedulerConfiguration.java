package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkProcessorSchedulerConfiguration {

  private final BulkRepository bulkRepository;
  private final BulkProcessor bulkProcessor;

  @Bean
  BulkProcessorScheduler bulkProcessorScheduler() {
    return new BulkProcessorScheduler(bulkProcessor, bulkRepository, bulkUpdater());
  }

  private BulkUpdater bulkUpdater() {
    return new BulkUpdater(bulkRepository);
  }
}
