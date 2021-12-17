package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.notification.NotificationModule;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ComponentScan(basePackageClasses = NotificationModule.class)
class UpdateNotificationsTest extends BaseJdbcTest {

  @Autowired
  private UpdateNotifications updateNotifications;

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  @Test
  @Sql(scripts = "populate_notifications.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "truncate_notifications.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  void update_resultSizeEqualsExpectedNumber() {
    updateNotifications.update(List.of(1L, 2L), NotificationStatus.SENT);
    var actualNotificationsWithSentStatus = jdbcTemplate.queryForObject(
        "SELECT count(*) FROM pb_notification pn WHERE pn.status = :status",
        Map.of("status", "SENT"), Integer.class);

    assertEquals(2, actualNotificationsWithSentStatus);
  }
}
