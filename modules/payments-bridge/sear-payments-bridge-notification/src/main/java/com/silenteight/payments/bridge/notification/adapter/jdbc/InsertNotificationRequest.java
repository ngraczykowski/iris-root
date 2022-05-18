package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
class InsertNotificationRequest {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_notification(notification_type_id, message, attachment, attachment_name, "
          + "status, subject)\n"
          + " VALUES (:notification_type_id, :message, :attachment, "
          + ":attachment_name, :status, :subject)";

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Transactional
  void insert(Notification notification) {

    var notificationParams = new HashMap<String, Object>();

    notificationParams.put("notification_type_id", notification.getType());
    notificationParams.put("message", notification.getMessage());
    notificationParams.put("attachment", notification.getAttachment());
    notificationParams.put("attachment_name", notification.getAttachmentName());
    notificationParams.put("status", NotificationStatus.NEW.toString());
    notificationParams.put("subject", notification.getSubject());

    namedParameterJdbcTemplate.update(SQL, notificationParams);
  }
}
