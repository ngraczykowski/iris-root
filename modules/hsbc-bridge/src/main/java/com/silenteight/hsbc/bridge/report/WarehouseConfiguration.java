package com.silenteight.hsbc.bridge.report;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class WarehouseConfiguration {

  private final WarehouseMessageSender messageSender;

  @Bean
  Warehouse warehouse() {
    return new Warehouse(messageSender);
  }
}
