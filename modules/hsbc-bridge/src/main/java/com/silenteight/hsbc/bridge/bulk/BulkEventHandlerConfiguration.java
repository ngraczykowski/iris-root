package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkEventHandlerConfiguration {

  private final BulkRepository bulkRepository;

  @Bean
  BulkEventHandler bulkEventHandler() {
    return new BulkEventHandler(bulkRepository);
  }
}
