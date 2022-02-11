package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchError;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class RabbitEventMapper {

  MessageBatchCompleted toMessageBatchCompleted(BatchCompleted event) {
    return MessageBatchCompleted.newBuilder()
        .setBatchId(event.id())
        .setAnalysisId(event.analysisId())
        .addAllAlertIds(event.alertIds())
        .setBatchMetadata(event.batchMetadata())
        .build();
  }

  MessageBatchError toMessageBatchError(BatchError event) {
    return MessageBatchError.newBuilder()
        .setBatchId(event.id())
        .setErrorDescription(event.errorDescription())
        .setBatchMetadata(Optional.ofNullable(event.batchMetadata()).orElse(""))
        .build();
  }
}
