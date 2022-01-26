package com.silenteight.warehouse.test.flows.production.v2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.warehouse.test.client.gateway.ProductionIndexClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class ProductionIndexClient {

  private final ProductionIndexClientGateway productionIndexClientGateway;
  private final AlertGenerator alertGenerator;
  private final Integer alertCount;

  @Scheduled(cron = "${test.generator.cron}")
  void send() {
    String requestId = randomUUID().toString();
    ProductionDataIndexRequest request = ProductionDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .addAllAlerts(getAlerts())
        .build();

    productionIndexClientGateway.indexRequest(request);
    log.info("Production msg sent, requestId={}", requestId);
  }

  private List<Alert> getAlerts() {
    return IntStream
        .range(0, alertCount)
        .boxed()
        .map(i -> alertGenerator.generateProduction())
        .collect(toList());
  }
}
