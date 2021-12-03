package com.silenteight.payments.bridge.notification.adapter.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.notification.model.NotificationStatus;

import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
class UpdateNotification {

  @Language("PostgreSQL")
  private static final String UPDATE_NOTIFICATION_STATUS =
      "UPDATE pb_notification SET status = :status\n"
          + " WHERE notification_id in (:notification_ids)";

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  void update(List<Long> ids, NotificationStatus status) {
    namedParameterJdbcTemplate.update(
        UPDATE_NOTIFICATION_STATUS, Map.of("notification_ids", ids, "status", status.toString()));
  }
}
