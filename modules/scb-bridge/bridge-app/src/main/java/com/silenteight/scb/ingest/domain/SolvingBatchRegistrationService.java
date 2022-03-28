package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.RegistrationAlertContext;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class SolvingBatchRegistrationService implements BatchRegistrationService {

  private final RegistrationApiClient registrationApiClient;

  @Override
  public void register(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationAlertContext context) {

    log.info("Registration of solving batch {}", internalBatchId);
    var alertCount = (long) alerts.size();
    var batch = Batch.of(internalBatchId, alertCount, context);
    registrationApiClient.registerBatch(batch);
    log.info("Registered of solving batch {} with {} alerts", internalBatchId, alertCount);
  }
}
