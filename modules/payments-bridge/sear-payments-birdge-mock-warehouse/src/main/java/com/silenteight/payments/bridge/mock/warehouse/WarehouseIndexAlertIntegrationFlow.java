package com.silenteight.payments.bridge.mock.warehouse;

import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.payments.bridge.warehouse.index.model.WarehouseIndexRequestedEvent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlow;

import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@RequiredArgsConstructor
@Profile("mockwarehouse")
public class WarehouseIndexAlertIntegrationFlow {

  private final MockWarehouseService mockWarehouseService;

  @Bean
  public IntegrationFlow warehouseIndexedAlert() {
    return from("warehouseOutboundChannel")
        .transform(WarehouseIndexRequestedEvent.class, WarehouseIndexRequestedEvent::getRequest)
        .handle(ProductionDataIndexRequest.class, (payload, headers) -> {
          mockWarehouseService.handle(payload);
          return payload;
        })
        .get();
  }
}
