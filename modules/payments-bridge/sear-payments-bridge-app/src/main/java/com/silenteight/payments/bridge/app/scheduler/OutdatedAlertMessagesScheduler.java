package com.silenteight.payments.bridge.app.scheduler;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.port.OutdatedAlertMessagesUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class OutdatedAlertMessagesScheduler {

  private final OutdatedAlertMessagesUseCase service;

  @Scheduled(fixedDelay = 1000, initialDelay = 1000)
  @SchedulerLock(name = "rejectOutdatedTask", lockAtMostFor = "1990", lockAtLeastFor = "990")
  void process() {
    // TODO: think about the case when task takes longer than 'lockAtMostFor'.
    while (true) {
      if (service.process(100))
        break;
    }
  }

}
