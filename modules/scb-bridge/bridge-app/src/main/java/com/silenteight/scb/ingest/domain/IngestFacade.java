package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.DataRetentionCommand;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IngestFacade {

  private final BatchRegistrationService batchRegistrationService;
  private final AlertRegistrationService alertRegistrationService;
  private final DataRetentionService dataRetentionService;

  public RegistrationResponse registerAlerts(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    batchRegistrationService.register(internalBatchId, alerts, registrationBatchContext);
    return alertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }

  public void performDataRetention(DataRetentionCommand command) {
    dataRetentionService.performDataRetention(command);
  }
}
