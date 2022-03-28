package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationAlertContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.scb.ingest.domain.model.AlertSource.LEARNING;
import static com.silenteight.scb.ingest.domain.model.Batch.Priority.LOW;

@Component
@RequiredArgsConstructor
public class AlertRegistrationFacade {

  private final BatchRegistrationService learningBatchRegistrationService;
  private final AlertRegistrationService learningAlertRegistrationService;
  private final BatchRegistrationService solvingBatchRegistrationService;
  private final AlertRegistrationService solvingAlertRegistrationService;

  public RegistrationResponse registerLearningAlert(String internalBatchId, List<Alert> alerts) {
    var registrationAlertContext = new RegistrationAlertContext(LOW, LEARNING);
    learningBatchRegistrationService.register(internalBatchId, alerts, registrationAlertContext);
    return learningAlertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }

  public RegistrationResponse registerSolvingAlert(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationAlertContext registrationAlertContext) {
    solvingBatchRegistrationService.register(internalBatchId, alerts, registrationAlertContext);
    return solvingAlertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }
}
