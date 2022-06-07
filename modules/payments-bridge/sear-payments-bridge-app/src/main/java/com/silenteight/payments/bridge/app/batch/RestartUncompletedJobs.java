package com.silenteight.payments.bridge.app.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(
    value = "pb.batch.restart-failed-jobs",
    havingValue = "true"
)
class RestartUncompletedJobs {

  private final JobMaintainer jobMaintainer;

  @Async
  @EventListener(classes = { ApplicationStartedEvent.class })
  public void onApplicationStartup() {
    log.info("Uncompleted job restart procedure has been started.");
    jobMaintainer.restartUncompletedJobs();
  }

}
