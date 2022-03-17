package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.AlertWithMatches;
import com.silenteight.scb.ingest.domain.model.RegistrationRequest;
import com.silenteight.scb.ingest.domain.model.RegistrationResponse;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class LearningAlertRegistrationService implements AlertRegistrationService {

  private final RegistrationApiClient registrationApiClient;
  private final AlertWitchMatchesExtractor alertWitchMatchesExtractor;

  @Override
  public RegistrationResponse registerAlertsAndMatches(String batchId, List<Alert> alerts) {
    log.info("Registration of {} learning alerts from batch {} ", alerts.size(), batchId);
    List<AlertWithMatches> alertWithMatches = alerts.stream()
        .map(alertWitchMatchesExtractor::extract)
        .toList();

    return registrationApiClient.registerAlertsAndMatches(
        RegistrationRequest.of(batchId, alertWithMatches));
  }
}
