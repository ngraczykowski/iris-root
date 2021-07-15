package com.silenteight.simulator.processing.alert.index.ack;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.domain.SimulationService;
import com.silenteight.simulator.processing.alert.index.amqp.listener.AckMessageHandler;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertQuery;
import com.silenteight.simulator.processing.alert.index.domain.IndexedAlertService;

import static com.silenteight.adjudication.api.v1.Analysis.State.DONE;

@RequiredArgsConstructor
public class FetchAckMessageUseCase implements AckMessageHandler {

  @NonNull
  private final AnalysisService analysisService;
  @NonNull
  private final IndexedAlertService indexedAlertService;
  @NonNull
  private final SimulationService simulationService;
  @NonNull
  private final IndexedAlertQuery indexedAlertQuery;

  @Override
  public void handle(DataIndexResponse request) {
    String requestId = request.getRequestId();
    indexedAlertService.ack(requestId);
    String analysisName = indexedAlertQuery.getAnalysisNameByRequestId(requestId);
    Analysis analysis = analysisService.getAnalysis(analysisName);
    if (isAnalysisDone(analysis))
      analyzeAckedMessages(analysisName);
  }

  private void analyzeAckedMessages(String analysisName) {
    if (areAllIndexedAlertsAcked(analysisName))
      analyzeAlertCount(analysisName);
  }

  private void analyzeAlertCount(String analysisName) {
    long sumAlertCount = indexedAlertQuery.sumAllAlertsCountWithAnalysisName(analysisName);
    long initialAlertCount = simulationService.countAllAlerts(analysisName);
    if (sumAlertCount == initialAlertCount)
      simulationService.finish(analysisName);
  }

  private boolean areAllIndexedAlertsAcked(String analysisName) {
    return indexedAlertQuery.areAllIndexedAlertsAcked(analysisName);
  }

  private static boolean isAnalysisDone(Analysis analysis) {
    return DONE == analysis.getState();
  }
}
