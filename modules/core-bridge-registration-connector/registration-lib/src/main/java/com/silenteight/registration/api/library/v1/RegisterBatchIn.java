package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.RegisterBatchRequest;

import java.util.Optional;

@Value
@Builder
public class RegisterBatchIn {

  String batchId;
  String batchMetadata;
  Long alertCount;

  RegisterBatchRequest toRegisterBatchRequest() {
    return RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertCount)
        .setBatchMetadata(Optional.ofNullable(batchMetadata).orElse(""))
        .build();
  }
}
