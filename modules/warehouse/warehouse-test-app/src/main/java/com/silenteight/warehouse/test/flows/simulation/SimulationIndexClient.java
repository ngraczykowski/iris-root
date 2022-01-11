package com.silenteight.warehouse.test.flows.simulation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.warehouse.test.client.gateway.SimulationIndexClientGateway;

import org.springframework.scheduling.annotation.Scheduled;

import static java.util.UUID.randomUUID;

@Slf4j
@RequiredArgsConstructor
public class SimulationIndexClient {

  private final SimulationIndexClientGateway simulationIndexClientGateway;
  private final AlertGenerator alertGenerator;
  private final Integer alertCount;

  @Scheduled(cron = "${test.generator.cron}")
  void init() {
    String requestId = randomUUID().toString();
    String analysisName = "analysis/" + randomUUID();

    SimulationDataIndexRequest request = SimulationDataIndexRequest.newBuilder()
        .setRequestId(requestId)
        .setAnalysisName(analysisName)
        .addAllAlerts(alertGenerator.generateSimulationAlerts(alertCount)).build();

    simulationIndexClientGateway.indexRequest(request);
    log.info("Simulation msg sent, requestId={}, analysis={}", requestId, analysisName);
  }
}
