package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.*;

import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
class ProcessSendingEmails implements ProcessSendingEmailsUseCase {

  private final FindNotificationsUseCase findNotificationsUseCase;
  private final UpdateNotificationsUseCase updateNotificationsUseCase;
  private final EmailSenderUseCase emailSenderUseCase;
  private final FindNotificationTypesUseCase findNotificationTypesUseCase;

  public void processSendingEmails() {
    findNotificationTypesUseCase.findAllNotificationTypes()
        .forEach(type -> {
          var notifications =
              findNotificationsUseCase.findNotificationsByTypeAndStatus(
                  type.getId(), NotificationStatus.NEW);
          sendEmails(notifications, type.getSubject());
        });
  }

  private void sendEmails(List<Notification> notifications, String subject) {
    notifications.forEach(notification -> {
      var id = notification.getId();
      try {
        emailSenderUseCase.sendEmail(id, subject, notification.getMessage(),
            notification.getAttachmentName(), notification.getAttachment());
        updateNotificationsUseCase.update(List.of(id), NotificationStatus.SENT);
        log.info("Notification {} have been sent in email", id);
      } catch (Exception ex) {
        log.error(
            "Notification {} haven't been sent in email. Message: {}, reason: {}.", id,
            ex.getMessage(), ex.getCause());
        updateNotificationsUseCase.update(List.of(id), NotificationStatus.ERROR);
      }
    });
  }
}
