package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkEventListenerConfiguration {

  private final BulkRepository bulkRepository;

  @Bean
  BulkEventListener bulkEventListener() {
    return new BulkEventListener(bulkUpdater());
  }

  private BulkUpdater bulkUpdater() {
    return new BulkUpdater(bulkRepository);
  }
}
