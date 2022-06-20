/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.iris.bridge.scb.ingest.domain.model.AlertWithMatches;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.iris.bridge.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.iris.bridge.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AlertRegistrationService {

  private final RegistrationApiClient registrationApiClient;
  private final AlertWitchMatchesExtractor alertWitchMatchesExtractor;

  public RegistrationResponse registerAlertsAndMatches(String batchId, List<Alert> alerts) {
    List<AlertWithMatches> alertWithMatches = alerts.stream()
        .map(alertWitchMatchesExtractor::extract)
        .toList();

    return registrationApiClient.registerAlertsAndMatches(
        RegistrationRequest.of(batchId, alertWithMatches));
  }
}
