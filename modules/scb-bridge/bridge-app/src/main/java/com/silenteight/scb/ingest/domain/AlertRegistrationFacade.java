package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertRegistrationFacade {

  private final BatchRegistrationService learningBatchRegistrationService;
  private final AlertRegistrationService learningAlertRegistrationService;
  private final BatchRegistrationService solvingBatchRegistrationService;
  private final AlertRegistrationService solvingAlertRegistrationService;

  public RegistrationResponse registerLearningAlert(String batchId, List<Alert> alerts) {
    learningBatchRegistrationService.register(batchId, alerts);
    return learningAlertRegistrationService.registerAlertsAndMatches(batchId, alerts);
  }

  public RegistrationResponse registerSolvingAlert(String batchId, List<Alert> alerts) {
    solvingBatchRegistrationService.register(batchId, alerts);
    return solvingAlertRegistrationService.registerAlertsAndMatches(batchId, alerts);
  }

}
