package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.NotificationModule;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.FindNotificationsUseCase;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ComponentScan(basePackageClasses = NotificationModule.class)
class FindNotificationsTest extends BaseJdbcTest {

  private static final String CSV_PROCESSED = "CSV_PROCESSED";
  private static final String CMAPI_PROCESSING_ERROR = "CMAPI_PROCESSING_ERROR";

  @Autowired
  private FindNotificationsUseCase findNotificationsUseCase;

  @Test
  @Sql(scripts = "populate_notifications.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "truncate_notifications.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  void findNotificationsByType_resultSizeEqualsExpectedNumber() {
    var foundCsvProcessedNotifications =
        findNotificationsUseCase.findNotificationsByTypeAndStatus(
            CSV_PROCESSED, NotificationStatus.NEW);
    var foundCmapiProcessingErrorNotifications =
        findNotificationsUseCase.findNotificationsByTypeAndStatus(
            CMAPI_PROCESSING_ERROR, NotificationStatus.NEW);
    assertEquals(2, foundCsvProcessedNotifications.size());
    assertEquals(3, foundCmapiProcessingErrorNotifications.size());
  }
}
