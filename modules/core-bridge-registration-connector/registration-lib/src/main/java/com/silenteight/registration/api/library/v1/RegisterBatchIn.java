package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.RegisterBatchRequest;

import java.util.Optional;

@Value
@Builder
public class RegisterBatchIn {

  String batchId;
  Long alertCount;
  Boolean isSimulation;
  String batchMetadata;
  Integer batchPriority;

  RegisterBatchRequest toRegisterBatchRequest() {
    return RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertCount)
        .setIsSimulation(Optional.ofNullable(isSimulation).orElse(false))
        .setBatchMetadata(Optional.ofNullable(batchMetadata).orElse(""))
        .setBatchPriority(Optional.ofNullable(batchPriority).orElse(0))
        .build();
  }
}
