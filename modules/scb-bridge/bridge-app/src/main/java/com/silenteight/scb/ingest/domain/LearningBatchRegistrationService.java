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
class LearningBatchRegistrationService implements BatchRegistrationService {

  private final RegistrationApiClient registrationApiClient;

  @Override
  public void register(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationAlertContext registrationAlertContext) {

    log.info("Registration of learning batch {}", internalBatchId);
    Long alertCount = (long) alerts.size();
    Batch batch = new Batch(internalBatchId, alertCount, registrationAlertContext.priority());
    registrationApiClient.registerBatch(batch);
    log.info("Registered of learning batch {} with {} alerts", internalBatchId, alertCount);
  }
}
