package com.silenteight.payments.bridge.app.scheduler;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.port.ProcessSendingEmailsUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class NotificationScheduler {

  private final ProcessSendingEmailsUseCase processSendingEmailsUseCase;

  @Scheduled(cron = "${pb.email-notification.cron}")
  @SchedulerLock(name = "notification_sending_lock", lockAtMostFor = "3600")
  void scheduleSendingEmails() {
    processSendingEmailsUseCase.processSendingEmails();
  }
}
