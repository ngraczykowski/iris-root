package com.silenteight.payments.bridge.common.event;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StopBatchJobEvent {

  Long jobExecutionId;
}
