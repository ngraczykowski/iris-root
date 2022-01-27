package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.service.EmailNotificationProperties;

import org.intellij.lang.annotations.Language;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(EmailNotificationProperties.class)
class FindNotification {

  private final EmailNotificationProperties properties;

  @Language("PostgreSQL")
  private static final String FIND_NOTIFICATIONS_BY_TYPE =
      "SELECT pn.notification_id, pn.created_at, pn.modified_at, pnt.notification_type_id, "
          + "pn.message, pn.attachment, pn.attachment_name, "
          + "pn.status, pn.subject FROM pb_notification pn\n"
          + " LEFT JOIN pb_notification_type pnt\n"
          + " ON pn.notification_type_id = pnt.notification_type_id\n"
          + " WHERE pn.notification_type_id = ?\n"
          + " AND pn.status = ?\n"
          + " ORDER BY pn.created_at ASC\n"
          + " LIMIT ?";

  private final JdbcTemplate jdbcTemplate;

  List<Notification> findNotificationsByTypeAndStatus(String type, NotificationStatus status) {

    return jdbcTemplate.query(FIND_NOTIFICATIONS_BY_TYPE, (rs, rowNum) -> mapper(rs), type,
        status.toString(), properties.getAmount());
  }

  static Notification mapper(ResultSet rs) throws SQLException {
    return Notification
        .builder()
        .id(rs.getLong("notification_id"))
        .type(rs.getString("notification_type_id"))
        .message(rs.getString("message"))
        .attachment(rs.getBytes("attachment"))
        .attachmentName(rs.getString("attachment_name"))
        .status(NotificationStatus.valueOf(rs.getString("status")))
        .subject(rs.getString("subject"))
        .build();
  }
}
