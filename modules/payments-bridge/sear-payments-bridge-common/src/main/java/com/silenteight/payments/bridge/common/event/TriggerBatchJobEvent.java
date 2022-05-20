package com.silenteight.payments.bridge.common.event;

import lombok.Builder;
import lombok.Value;

import org.springframework.batch.core.JobParameters;

@Value
@Builder
public class TriggerBatchJobEvent {

  String jobName;

  JobParameters parameters;
}
