package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.AddAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertsToAnalysisUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;

import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AddAlertsToAnalysisService implements AddAlertsToAnalysisUseCase {

  private final GetCurrentAnalysisUseCase getCurrentAnalysisUseCase;
  private final AnalysisClientPort analysisClient;

  @Override
  public void addAlerts(AddAlertRequest addAlertRequest) {
    var analysisId = getCurrentAnalysisUseCase.getOrCreateAnalysis();
    analysisClient.addAlertToAnalysis(
        BatchAddAlertsRequest
            .newBuilder()
            .setAnalysis("analysis/" + analysisId)
            .addAllAnalysisAlerts(
                addAlertRequest.getAlertNames()
                    .stream()
                    .map(a -> AnalysisAlert.newBuilder().setAlert(a).build())
                    .collect(
                        Collectors.toList()))
            .build());
  }
}
