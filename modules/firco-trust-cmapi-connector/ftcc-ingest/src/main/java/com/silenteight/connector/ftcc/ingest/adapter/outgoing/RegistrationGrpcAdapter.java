package com.silenteight.connector.ftcc.ingest.adapter.outgoing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.Batch;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.registration.api.library.v1.RegisterBatchIn;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

@RequiredArgsConstructor
class RegistrationGrpcAdapter implements RegistrationApiClient {

  @NonNull
  private final RegistrationServiceClient registrationServiceClient;

  public void registerBatch(Batch batch) {
    registrationServiceClient.registerBatch(toRegisterBatchIn(batch));
  }

  private static RegisterBatchIn toRegisterBatchIn(Batch batch) {
    return RegisterBatchIn.builder()
        .batchId(batch.getBatchId())
        .alertCount(batch.getAlertsCount())
        .build();
  }
}
