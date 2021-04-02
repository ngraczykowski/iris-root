package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.repository.BulkRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class BulkStatusUpdaterConfiguration {

  private final BulkRepository bulkRepository;
  private final BulkItemRepository bulkItemRepository;
  private final ApplicationEventPublisher publisher;

  @Bean
  BulkItemStatusUpdater bulkItemStatusUpdater() {
    return new BulkItemStatusUpdater(bulkItemRepository, publisher);
  }

  @Bean
  BulkStatusUpdater bulkStatusUpdater() {
    return new BulkStatusUpdater(bulkRepository);
  }
}
