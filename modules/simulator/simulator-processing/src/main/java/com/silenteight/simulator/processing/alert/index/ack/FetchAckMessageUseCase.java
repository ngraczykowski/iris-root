package com.silenteight.simulator.processing.alert.index.ack;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.listener.AckMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static java.util.List.of;

@Slf4j
@RequiredArgsConstructor
public class FetchAckMessageUseCase implements AckMessageHandler {

  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final SimulationService simulationService;
  @NonNull
  private final IndexedAlertQuery indexedAlertQuery;

  @Override
  public void handle(DataIndexResponse request) {
    String requestId = request.getRequestId();
    log.debug("Data feed acknowledged: requestId={}", requestId);

    indexedAlertService.ack(requestId);
    String analysisName = indexedAlertQuery.getAnalysisNameByRequestId(requestId);
    log.debug("Found analysisName={} for requestId={}", analysisName, requestId);

    analyzeAckedMessages(analysisName);
  }

  private void analyzeAckedMessages(String analysisName) {
    if (areAllIndexedAlertsAcked(analysisName)) {
      log.debug("All indexed alerts for analysisName={} are 'ACKED'", analysisName);
      simulationService.finish(analysisName);
      log.info("Simulation with analysisName={} is finished", analysisName);
    }
  }

  private boolean areAllIndexedAlertsAcked(String analysisName) {
    return indexedAlertQuery.count(analysisName, of(SENT)) == 0;
  }
}
