package com.silenteight.warehouse.test.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SimulationIndexClient {

  private final SimulationIndexClientGateway simulationIndexClientGateway;
  private final AlertGenerator alertGenerator;
  private final String analysisName;

  @Scheduled(cron = "${test.generator.cron}")
  void init() {
    String requestId = randomUUID().toString();
    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(analysisName)
        .addAllAlerts(getAlerts()).build();

    simulationIndexClientGateway.indexRequest(request);
    log.info("Simulation msg sent, requestId={}, analysis={}", requestId, analysisName);
  }

  private List<Alert> getAlerts() {
    return IntStream
        .range(0, 1000)
        .boxed()
        .map(i -> alertGenerator.generateSimulation())
        .collect(toList());
  }
}
