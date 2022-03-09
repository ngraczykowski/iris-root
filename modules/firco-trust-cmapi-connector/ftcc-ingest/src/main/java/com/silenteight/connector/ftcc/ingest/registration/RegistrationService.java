package com.silenteight.connector.ftcc.ingest.registration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.dto.input.RequestDto;
import com.silenteight.registration.api.library.v1.RegisterBatchIn;
import com.silenteight.registration.api.library.v1.RegistrationServiceClient;

@RequiredArgsConstructor
public class RegistrationService {

  @NonNull
  private final RegistrationServiceClient registrationServiceClient;

  public void registerBatch(@NonNull String batchId, @NonNull RequestDto request) {
    registrationServiceClient.registerBatch(toRegisterBatchIn(batchId, request));
  }

  private static RegisterBatchIn toRegisterBatchIn(String batchId, RequestDto request) {
    return RegisterBatchIn.builder()
        .batchId(batchId)
        .alertCount((long) request.getAlerts().size())
        .build();
  }
}
