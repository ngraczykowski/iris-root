package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.scb.ingest.domain.model.Batch.Priority.LOW;
import static com.silenteight.scb.ingest.domain.model.BatchSource.LEARNING;

@Component
@RequiredArgsConstructor
public class AlertRegistrationFacade {

  private final BatchRegistrationService learningBatchRegistrationService;
  private final AlertRegistrationService learningAlertRegistrationService;
  private final BatchRegistrationService solvingBatchRegistrationService;
  private final AlertRegistrationService solvingAlertRegistrationService;

  public RegistrationResponse registerLearningAlerts(String internalBatchId, List<Alert> alerts) {
    var registrationBatchContext = new RegistrationBatchContext(LOW, LEARNING);
    learningBatchRegistrationService.register(internalBatchId, alerts, registrationBatchContext);
    return learningAlertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }

  public RegistrationResponse registerSolvingAlerts(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    solvingBatchRegistrationService.register(internalBatchId, alerts, registrationBatchContext);
    return solvingAlertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }
}
