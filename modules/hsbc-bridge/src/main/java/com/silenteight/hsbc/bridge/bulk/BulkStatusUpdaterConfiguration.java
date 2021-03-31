package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkStatusUpdaterConfiguration {

  private final BulkItemRepository bulkItemRepository;

  @Bean
  BulkItemStatusUpdater bulkItemStatusUpdater() {
    return new BulkItemStatusUpdater(bulkItemRepository);
  }
}
