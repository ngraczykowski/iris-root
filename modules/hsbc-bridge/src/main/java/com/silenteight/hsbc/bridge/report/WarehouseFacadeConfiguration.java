package com.silenteight.hsbc.bridge.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
class WarehouseFacadeConfiguration {

  private final AlertFinder alertFinder;
  private final WarehouseMessageSender messageSender;

  @Bean
  public WarehouseFacade warehouseFacade() {
    return new WarehouseFacade(alertFinder, messageSender);
  }
}
