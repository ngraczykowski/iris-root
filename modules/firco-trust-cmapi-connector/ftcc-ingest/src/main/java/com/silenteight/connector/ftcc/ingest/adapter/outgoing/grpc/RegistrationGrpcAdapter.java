package com.silenteight.connector.ftcc.ingest.adapter.outgoing.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.domain.Batch;
import com.silenteight.connector.ftcc.ingest.domain.port.outgoing.RegistrationApiClient;
import com.silenteight.registration.api.library.v1.RegisterBatchIn;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

@RequiredArgsConstructor
class RegistrationGrpcAdapter implements RegistrationApiClient {

  private static final int SOLVING_PRIORITY = 10;
  private static final int LEARNING_PRIORITY = 1;

  @NonNull
  private final RegistrationServiceClient registrationServiceClient;

  public void registerBatch(Batch batch) {
    registrationServiceClient.registerBatch(toRegisterBatchIn(batch));
  }

  private static RegisterBatchIn toRegisterBatchIn(Batch batch) {
    return RegisterBatchIn.builder()
        .batchId(batch.getBatchId())
        .alertCount(batch.getAlertsCount())
        .batchPriority(evaluatePriority(batch.isSimulation()))
        .isSimulation(batch.isSimulation())
        .build();
  }

  private static int evaluatePriority(boolean isSimulation) {
    return isSimulation ? LEARNING_PRIORITY : SOLVING_PRIORITY;
  }
}
