package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AddAlertToAnalysisUseCase;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class AddAlertToAnalysisService implements AddAlertToAnalysisUseCase {

  private final CreateAlertsService createAlertsService;
  private final GetCurrentAnalysisUseCase getCurrentAnalysisUseCase;
  private final AnalysisClientPort analysisClient;

  public void addAlertToAnalysis(List<RegisterAlertRequest> registerAlertRequest) {
    var alertName = createAlertsService.createAlert(registerAlertRequest);
    var analysisId = getCurrentAnalysisUseCase.getOrCreateAnalysis();
    analysisClient.addAlertToAnalysis(
        BatchAddAlertsRequest
            .newBuilder()
            .setAnalysis("analysis/" + analysisId)
            .addAllAnalysisAlerts(
                alertName.stream().map(a -> AnalysisAlert.newBuilder().setAlert(a).build()).collect(
                    Collectors.toList()))
            .build());
  }
}
