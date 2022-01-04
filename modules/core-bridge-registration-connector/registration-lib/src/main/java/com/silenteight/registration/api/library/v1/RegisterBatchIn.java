package com.silenteight.registration.api.library.v1;

import lombok.Builder;

import com.silenteight.proto.registration.api.v1.RegisterBatchRequest;

import java.util.Optional;

public record RegisterBatchIn(String batchId, Long alertCount,
                              String batchMetadata) {

  @Builder
  public RegisterBatchIn {}

  RegisterBatchRequest toRegisterBatchRequest() {
    return RegisterBatchRequest.newBuilder()
        .setBatchId(batchId)
        .setAlertCount(alertCount)
        .setBatchMetadata(Optional.ofNullable(batchMetadata).orElse(""))
        .build();
  }
}
