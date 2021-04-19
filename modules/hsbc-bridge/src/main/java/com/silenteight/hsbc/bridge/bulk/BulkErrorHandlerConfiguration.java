package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkErrorHandlerConfiguration {

  private final BulkRepository bulkRepository;

  @Bean
  BulkErrorHandler bulkErrorHandler() {
    return new BulkErrorHandler(bulkRepository);
  }
}
