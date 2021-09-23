package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.RegisterAlertResponse;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.RegisterAlertUseCase;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class RegisterAlertService implements RegisterAlertUseCase {

  private final CreateAlertsService createAlertsService;
  private final GetCurrentAnalysisUseCase getCurrentAnalysisUseCase;
  private final AnalysisClientPort analysisClient;

  public List<RegisterAlertResponse> register(List<RegisterAlertRequest> registerAlertRequest) {
    var registeredAlertMatches = new ArrayList<RegisterAlertResponse>();
    registerAlertRequest.forEach(
        ar -> registeredAlertMatches.add(createAlertsService.createAlert(ar)));

    var analysisId = getCurrentAnalysisUseCase.getOrCreateAnalysis();
    analysisClient.addAlertToAnalysis(
        BatchAddAlertsRequest
            .newBuilder()
            .setAnalysis("analysis/" + analysisId)
            .addAllAnalysisAlerts(
                registeredAlertMatches
                    .stream()
                    .map(a -> AnalysisAlert.newBuilder().setAlert(a.getAlertName()).build())
                    .collect(
                        Collectors.toList()))
            .build());

    return registeredAlertMatches;
  }
}
