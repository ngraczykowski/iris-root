package com.silenteight.payments.bridge.ae.alertregistration.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.BatchAddAlertsRequest;
import com.silenteight.payments.bridge.ae.alertregistration.domain.TriggerAlertRequest;
import com.silenteight.payments.bridge.ae.alertregistration.port.AnalysisClientPort;
import com.silenteight.payments.bridge.ae.alertregistration.port.TriggerAlertAnalysisUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
class TriggerAlertAnalysisService implements TriggerAlertAnalysisUseCase {

  private final AnalysisClientPort analysisClient;
  private final AnalysisProvider analysisProvider;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public void triggerAlertAnalysis(TriggerAlertRequest triggerAlertRequest) {
    var analysisName = this.analysisProvider.currentAnalysis();
    log.info("Current analysis: {}", analysisName);

    analysisClient.addAlertToAnalysis(
        BatchAddAlertsRequest
            .newBuilder()
            .setAnalysis(analysisName)
            .addAllAnalysisAlerts(
                triggerAlertRequest.getAlertNames()
                    .stream()
                    .map(a -> AnalysisAlert.newBuilder().setAlert(a).build())
                    .collect(
                        Collectors.toList()))
            .build());

    log.info("Alerts {} added to analysis [{}]",
        Arrays.toString(triggerAlertRequest.getAlertNames().toArray()), analysisName);
  }
}
