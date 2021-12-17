package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.NotificationType;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
class FindNotificationType {

  @Language("PostgreSQL")
  private static final String FIND_NOTIFICATION_TYPES =
      "SELECT pnt.notification_type_id, pnt.subject"
          + " FROM pb_notification_type pnt";

  private final JdbcTemplate jdbcTemplate;

  List<NotificationType> findAllNotificationTypes() {
    return jdbcTemplate.query(FIND_NOTIFICATION_TYPES, (rs, rowNum) -> mapper(rs));
  }

  static NotificationType mapper(ResultSet rs) throws SQLException {
    return NotificationType
        .builder()
        .id(rs.getString(1))
        .subject(rs.getString(2))
        .build();
  }
}
