package com.silenteight.payments.bridge.app.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.event.TriggerBatchJobEvent;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.STORE_CSV_JOB_NAME;

@Service
@RequiredArgsConstructor
@Slf4j
class LearningRunnerService {

  private final ApplicationEventPublisher applicationEventPublisher;

  void trigger(LearningFileEntity file) {
    log.info("Trigger learning of a file: {}", file.getFileName());
    applicationEventPublisher.publishEvent(
        TriggerBatchJobEvent
            .builder()
            .jobName(STORE_CSV_JOB_NAME)
            .parameters(file.toJobParameters())
            .build());
  }
}
