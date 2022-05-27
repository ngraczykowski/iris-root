package com.silenteight.scb.ingest.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;
import com.silenteight.scb.ingest.domain.model.Batch;
import com.silenteight.scb.ingest.domain.model.RegistrationBatchContext;
import com.silenteight.scb.ingest.domain.port.outgoing.RegistrationApiClient;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class BatchRegistrationService {

  private final RegistrationApiClient registrationApiClient;

  public void register(
      String internalBatchId,
      List<Alert> alerts,
      RegistrationBatchContext registrationBatchContext) {

    var alertCount = (long) alerts.size();
    var batch = Batch.of(internalBatchId, alertCount, registrationBatchContext);
    registrationApiClient.registerBatch(batch);
  }
}
