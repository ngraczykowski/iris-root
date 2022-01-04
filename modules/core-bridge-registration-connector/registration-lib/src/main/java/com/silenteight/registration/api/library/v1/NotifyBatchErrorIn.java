package com.silenteight.registration.api.library.v1;

import lombok.Builder;

import com.silenteight.proto.registration.api.v1.NotifyBatchErrorRequest;

import java.util.Optional;

public record NotifyBatchErrorIn(String batchId, String errorDescription,
                                 String batchMetadata) {

  @Builder
  public NotifyBatchErrorIn {}

  NotifyBatchErrorRequest toNotifyBatchErrorRequest() {
    return NotifyBatchErrorRequest.newBuilder()
        .setBatchId(batchId)
        .setErrorDescription(Optional.ofNullable(errorDescription).orElse(""))
        .setBatchMetadata(Optional.ofNullable(batchMetadata).orElse(""))
        .build();
  }
}
