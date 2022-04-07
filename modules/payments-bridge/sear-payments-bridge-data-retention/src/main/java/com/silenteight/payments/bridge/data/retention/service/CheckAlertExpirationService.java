package com.silenteight.payments.bridge.data.retention.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;
import com.silenteight.payments.bridge.data.retention.port.CheckAlertExpirationUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.batch.core.JobParameters;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.data.retention.service.DataRetentionConstants.SEND_ALERTS_EXPIRED_JOB_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
class CheckAlertExpirationService implements CheckAlertExpirationUseCase {

  private final ApplicationEventPublisher applicationEventPublisher;

  @Async
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  public void execute() {
    var triggerEvent = TriggerBatchJobEvent
        .builder()
        .jobName(SEND_ALERTS_EXPIRED_JOB_NAME)
        .parameters(new JobParameters())
        .build();
    applicationEventPublisher.publishEvent(triggerEvent);
  }
}
