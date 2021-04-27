package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkEventListenerConfiguration {

  private final BulkItemRepository bulkItemRepository;
  private final BulkRepository bulkRepository;

  @Bean
  BulkEventListener bulkEventListener() {
    return new BulkEventListener(bulkUpdater(), bulkItemStatusUpdater());
  }

  private BulkUpdater bulkUpdater() {
    return new BulkUpdater(bulkRepository);
  }

  private BulkItemStatusUpdater bulkItemStatusUpdater() {
    return new BulkItemStatusUpdater(bulkItemRepository, bulkStatusUpdater());
  }

  private BulkStatusUpdater bulkStatusUpdater() {
    return new BulkStatusUpdater(bulkRepository);
  }
}
