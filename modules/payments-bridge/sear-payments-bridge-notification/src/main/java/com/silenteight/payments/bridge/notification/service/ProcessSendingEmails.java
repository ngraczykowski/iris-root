package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.model.SendEmailRequest;
import com.silenteight.payments.bridge.notification.port.*;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@RequiredArgsConstructor
@Slf4j
@Component
@EnableConfigurationProperties(EmailNotificationProperties.class)
class ProcessSendingEmails implements ProcessSendingEmailsUseCase {

  private final FindNotificationsUseCase findNotificationsUseCase;
  private final UpdateNotificationsUseCase updateNotificationsUseCase;
  private final EmailSenderUseCase emailSenderUseCase;
  private final FindNotificationTypesUseCase findNotificationTypesUseCase;
  private final CmapiNotificationCreatorService cmapiNotificationCreatorService;
  private final EmailNotificationProperties emailNotificationProperties;

  private static final String CMAPI_PROCESSING_ERROR = "CMAPI_PROCESSING_ERROR";
  private static final String CSV_PROCESSED = "CSV_PROCESSED";
  private static final String CMAPI_PROCESSING_ERROR_ATTACHMENT_NAME = "CMAPI_ERRORS.zip";

  public void processSendingEmails() {
    findNotificationTypesUseCase.findAllNotificationTypes().forEach(type -> {
      var notifications = findNotificationsUseCase.findNotificationsByTypeAndStatus(
          type.getId(),
          NotificationStatus.NEW);
      if (!notifications.isEmpty()) {
        sendEmails(notifications, type.getId());
      }
    });
  }

  private void sendEmails(List<Notification> notifications, String typeId) {

    if (CMAPI_PROCESSING_ERROR.equals(typeId)) {
      handleSendingCmapiProcessingNotifications(notifications);
    } else {
      handleSendingDefaultNotifications(notifications, typeId);
    }
  }

  private void handleSendingCmapiProcessingNotifications(List<Notification> notifications) {

    var ids = extractIds(notifications);

    if (emailNotificationProperties.isCmapiEnabled()) {

      var attachments = extractAttachments(notifications);

      var message = extractMessage(notifications);

      var mergedAttachment = createMergedAttachment(attachments);

      var attachmentName = mergedAttachment == null ? null : CMAPI_PROCESSING_ERROR_ATTACHMENT_NAME;

      var subject = createSubject(notifications);

      var sendEmailRequest = SendEmailRequest
          .builder()
          .ids(ids)
          .subject(subject)
          .htmlText(message)
          .attachmentName(attachmentName)
          .attachment(mergedAttachment)
          .build();

      send(sendEmailRequest, ids);
    } else {
      updateNotificationsAsDisabled(ids);
    }
  }

  @Nullable
  private byte[] createMergedAttachment(List<byte[]> attachments) {
    return Optional
        .of(attachments)
        .filter(a -> !a.isEmpty())
        .map(cmapiNotificationCreatorService::mergeCsvAttachments)
        .orElse(null);
  }

  @Nonnull
  private String extractMessage(List<Notification> notifications) {
    return notifications.stream()
        .findAny()
        .map(Notification::getMessage)
        .orElse("");
  }

  @Nonnull
  private static List<byte[]> extractAttachments(List<Notification> notifications) {
    return notifications
        .stream()
        .map(Notification::getAttachment)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Nonnull
  private List<Long> extractIds(List<Notification> notifications) {
    return notifications.stream()
        .map(Notification::getId)
        .collect(Collectors.toList());
  }

  private void handleSendingDefaultNotifications(
      List<Notification> notifications, String typeId) {
    if (CSV_PROCESSED.equals(typeId) && emailNotificationProperties.isLearningEnabled()) {
      notifications.forEach(notification -> {
        var ids = List.of(notification.getId());
        var sendEmailRequest = SendEmailRequest
            .builder()
            .ids(ids)
            .subject(notification.getSubject())
            .htmlText(notification.getMessage())
            .attachmentName(notification.getAttachmentName())
            .attachment(notification.getAttachment())
            .build();
        send(sendEmailRequest, ids);
      });
    } else {
      var ids = extractIds(notifications);
      updateNotificationsAsDisabled(ids);
    }
  }

  @Nonnull
  private String createSubject(List<Notification> notifications) {
    return notifications.stream()
        .map(Notification::getSubject)
        .findFirst()
        .orElse("");
  }

  private void send(SendEmailRequest sendEmailRequest, List<Long> ids) {
    try {
      emailSenderUseCase.sendEmail(sendEmailRequest);
      updateNotificationsUseCase.update(ids, NotificationStatus.SENT);
      log.info("Notifications ids={} have been sent in email", ids);
    } catch (Exception ex) {
      log.error("Notifications ids={} haven't been sent in email. Message: {}, reason: {}.", ids,
          ex.getMessage(), ex.getCause());
      updateNotificationsUseCase.update(ids, NotificationStatus.ERROR);
    }
  }

  private void updateNotificationsAsDisabled(List<Long> ids) {
    updateNotificationsUseCase.update(ids, NotificationStatus.DISABLED);
    log.info("Notifications ids={} have been set as disabled", ids);
  }
}
