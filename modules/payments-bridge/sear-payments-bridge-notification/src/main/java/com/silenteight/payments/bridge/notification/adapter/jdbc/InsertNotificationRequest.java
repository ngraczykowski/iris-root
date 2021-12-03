package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
class InsertNotificationRequest {

  @Language("PostgreSQL")
  private static final String SQL =
      "INSERT INTO pb_notification(notification_type_id, message, attachment, attachment_name, "
          + "status)\n"
          + " VALUES (?, ?, ?, ?, ?)";

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  void insert(Notification notification) {
    namedParameterJdbcTemplate.update(SQL, Map.of(
        "notification_type_id", notification.getType(),
        "message", notification.getMessage(),
        "attachment", notification.getAttachment(),
        "attachment_name", notification.getAttachmentName(),
        "status", NotificationStatus.NEW.toString()));
  }
}
