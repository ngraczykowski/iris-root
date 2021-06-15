package com.silenteight.warehouse.test.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import static java.util.List.of;
import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class ProductionIndexClient {

  private final ProductionIndexClientGateway productionIndexClientGateway;
  private final AlertGenerator alertGenerator;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();
    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .addAllAlerts(of(alertGenerator.generate()))
        .build();

    productionIndexClientGateway.indexRequest(request);
    log.info("Production msg sent, requestId={}", requestId);
  }
}
