package com.silenteight.hsbc.bridge.report;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@Slf4j
class WarehouseMessageSenderMockConfiguration {

  @Bean
  WarehouseMessageSender warehouseMessageSender() {
    return dataIndexRequest -> log.warn("DataIndexRequest has been sent");
  }
}
