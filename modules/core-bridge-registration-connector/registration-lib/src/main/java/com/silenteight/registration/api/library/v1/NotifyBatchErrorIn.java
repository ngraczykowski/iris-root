package com.silenteight.registration.api.library.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.registration.api.v1.NotifyBatchErrorRequest;

import java.util.Optional;

@Value
@Builder
public class NotifyBatchErrorIn {

  String batchId;
  String errorDescription;

  NotifyBatchErrorRequest toNotifyBatchErrorRequest() {
    return NotifyBatchErrorRequest.newBuilder()
        .setBatchId(batchId)
        .setErrorDescription(Optional.ofNullable(errorDescription).orElse(""))
        .build();
  }
}
