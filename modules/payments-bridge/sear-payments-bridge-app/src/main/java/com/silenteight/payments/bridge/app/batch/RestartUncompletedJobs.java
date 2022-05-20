package com.silenteight.payments.bridge.app.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
class RestartUncompletedJobs {

  private final JobMaintainer jobMaintainer;

  @EventListener(classes = { ApplicationStartedEvent.class })
  public void onApplicationStartup() {
    log.info("Uncompleted job restart procedure has been started.");
    jobMaintainer.restartUncompletedJobs();
  }

}
