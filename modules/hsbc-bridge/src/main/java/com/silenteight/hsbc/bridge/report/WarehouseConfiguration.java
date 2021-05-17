package com.silenteight.hsbc.bridge.report;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
class WarehouseConfiguration {

  @Bean
  @Profile("dev")
  public WarehouseClient warehouseClientMock() {
    return alerts -> log.warn("Alerts have been sent to warehouse, size={}", alerts.size());
  }
}
