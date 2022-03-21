package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.Batch.Priority;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class LearningBatchRegistrationService implements BatchRegistrationService {

  private final RegistrationApiClient registrationApiClient;

  @Override
  public void register(String batchId, List<Alert> alerts, Priority priority) {
    log.info("Registration of learning batch {}", batchId);
    Long alertCount = (long) alerts.size();
    Batch batch = new Batch(batchId, alertCount, priority);
    registrationApiClient.registerBatch(batch);
    log.info("Registered of learning batch {} with {} alerts", batchId, alertCount);
  }
}
