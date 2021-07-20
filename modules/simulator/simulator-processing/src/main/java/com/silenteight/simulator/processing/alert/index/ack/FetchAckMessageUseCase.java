package com.silenteight.simulator.processing.alert.index.ack;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.details.SimulationDetailsQuery;
import com.silenteight.simulator.management.details.dto.SimulationDetailsDto;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.listener.AckMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;
import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;
import static java.util.List.of;

@Slf4j
@RequiredArgsConstructor
public class FetchAckMessageUseCase implements AckMessageHandler {

  @NonNull
  private final AnalysisService analysisService;
  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final SimulationDetailsQuery simulationQuery;
  @NonNull
  private final DatasetMetadataService datasetService;
  @NonNull
  private final SimulationService simulationService;
  @NonNull
  private final IndexedAlertQuery indexedAlertQuery;

  @Override
  public void handle(DataIndexResponse request) {
    String requestId = request.getRequestId();
    log.info("Data feed acknowledged: requestId=" + requestId);

    indexedAlertService.ack(requestId);
    String analysisName = indexedAlertQuery.getAnalysisNameByRequestId(requestId);
    Analysis analysis = analysisService.getAnalysis(analysisName);
    if (isAnalysisDone(analysis))
      analyzeAckedMessages(analysisName);
  }

  private static boolean isAnalysisDone(Analysis analysis) {
    return DONE == analysis.getState();
  }

  private void analyzeAckedMessages(String analysisName) {
    if (areAllIndexedAlertsAcked(analysisName))
      analyzeAlertCount(analysisName);
  }

  private boolean areAllIndexedAlertsAcked(String analysisName) {
    return indexedAlertQuery.count(analysisName, of(SENT)) == 0;
  }

  private void analyzeAlertCount(String analysisName) {
    long sumAlertCount = indexedAlertQuery.sumAllAlertsCountWithAnalysisName(analysisName);
    SimulationDetailsDto simulation = simulationQuery.get(analysisName);
    long initialAlertCount = datasetService.countAllAlerts(simulation.getDatasets());
    if (sumAlertCount == initialAlertCount) {
      simulationService.finish(analysisName);
      log.info("Simulation with analysisName=" + analysisName + " is finished");
    }
  }
}
