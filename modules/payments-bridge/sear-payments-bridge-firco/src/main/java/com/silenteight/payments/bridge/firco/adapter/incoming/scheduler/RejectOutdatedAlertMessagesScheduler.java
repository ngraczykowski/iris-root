package com.silenteight.payments.bridge.firco.adapter.incoming.scheduler;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.alertmessage.port.RejectOutdatedAlertMessagesUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties(RejectOutdatedAlertMessageSchedulerProperties.class)
@RequiredArgsConstructor
@Component
class RejectOutdatedAlertMessagesScheduler {

  private final RejectOutdatedAlertMessagesUseCase service;
  private final RejectOutdatedAlertMessageSchedulerProperties properties;

  @Scheduled(fixedDelay = 1000, initialDelay = 1000)
  @SchedulerLock(name = "rejectOutdatedTask",
      lockAtMostFor = "1990", lockAtLeastFor = "990")
  void process() {
    // TODO: think about the case when task takes longer than 'lockAtMostFor'.
    while (true) {
      if (service.process(properties.getChunkSize()))
        break;
    }
  }

}
