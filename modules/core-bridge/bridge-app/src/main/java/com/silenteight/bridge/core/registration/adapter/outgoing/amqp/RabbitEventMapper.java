package com.silenteight.bridge.core.registration.adapter.outgoing.amqp;

import com.silenteight.bridge.core.registration.domain.model.*;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchDelivered;
import com.silenteight.proto.registration.api.v1.MessageBatchError;
import com.silenteight.proto.registration.api.v1.MessageNotifyBatchTimedOut;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class RabbitEventMapper {

  MessageBatchCompleted toMessageBatchCompleted(SolvingBatchCompleted event) {
    return MessageBatchCompleted.newBuilder()
        .setBatchId(event.id())
        .setAnalysisName(event.analysisName())
        .setBatchMetadata(event.batchMetadata())
        .build();
  }

  MessageBatchCompleted toMessageBatchCompleted(SimulationBatchCompleted event) {
    return MessageBatchCompleted.newBuilder()
        .setBatchId(event.id())
        .setAnalysisName(Optional.ofNullable(event.analysisName()).orElse(""))
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

  MessageNotifyBatchTimedOut toMessageNotifyBatchTimedOut(BatchTimedOut event) {
    return MessageNotifyBatchTimedOut.newBuilder()
        .setAnalysisName(event.analysisName())
        .addAllAlertNames(event.alertNames())
        .build();
  }

  MessageBatchDelivered toMessageBatchDelivered(BatchDelivered event) {
    return MessageBatchDelivered.newBuilder()
        .setBatchId(event.batchId())
        .setAnalysisName(event.analysisName())
        .build();
  }
}
