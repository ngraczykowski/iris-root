package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.registration.internal.proto.v1.RegisterBatchRequest;

@Value
@Builder
public class RegisterBatchIn {

  String batchId;
  Long alertCount;

  RegisterBatchRequest toRegisterBatchRequest() {
    return RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertCount)
        .build();
  }
}
