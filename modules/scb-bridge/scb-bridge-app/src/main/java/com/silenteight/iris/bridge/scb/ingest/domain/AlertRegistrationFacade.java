/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlertRegistrationFacade {

  private final BatchRegistrationService batchRegistrationService;
  private final AlertRegistrationService alertRegistrationService;

  public RegistrationResponse registerAlerts(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {
    batchRegistrationService.register(internalBatchId, alerts, registrationBatchContext);
    return alertRegistrationService.registerAlertsAndMatches(internalBatchId, alerts);
  }
}
