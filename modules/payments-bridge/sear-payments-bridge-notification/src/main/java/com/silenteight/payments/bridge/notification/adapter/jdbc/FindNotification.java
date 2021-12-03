package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
class FindNotification {

  private static final int LIMIT = 10;

  @Language("PostgreSQL")
  private static final java.lang.String FIND_NOTIFICATIONS_BY_TYPE =
      "SELECT pn.notification_id, pn.created_at, pn.modified_at, pnt.notification_type_id, "
          + "pn.message, pn.attachment, pn.attachment_name,"
          + " status FROM pb_notification pn\n"
          + " LEFT JOIN pb_notification_type pnt\n"
          + " ON pn.notification_type_id = pnt.notification_type_id\n"
          + " WHERE pn.notification_type_id = ?\n"
          + " AND pn.status = ?\n"
          + " ORDER BY pn.created_at ASC\n"
          + " LIMIT ?";

  private final JdbcTemplate jdbcTemplate;

  List<Notification> findNotificationsByTypeAndStatus(String type, NotificationStatus status) {

    return jdbcTemplate.query(
        FIND_NOTIFICATIONS_BY_TYPE, (rs, rowNum) -> mapper(rs), type, status.toString(), LIMIT);
  }

  static Notification mapper(ResultSet rs) throws SQLException {
    return Notification.builder()
        .id(rs.getLong(1))
        .type(String.valueOf(rs.getString(4)))
        .message(rs.getString(5))
        .attachment(rs.getBytes(6))
        .attachmentName(rs.getString(7))
        .status(NotificationStatus.valueOf(rs.getString(8)))
        .build();
  }
}
