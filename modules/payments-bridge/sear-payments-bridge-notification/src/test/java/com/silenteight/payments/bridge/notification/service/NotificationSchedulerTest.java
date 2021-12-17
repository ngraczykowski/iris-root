package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.notification.NotificationModule;
import com.silenteight.payments.bridge.notification.model.NotificationStatus;
import com.silenteight.payments.bridge.notification.port.EmailSenderUseCase;
import com.silenteight.payments.bridge.notification.port.FindNotificationsUseCase;
import com.silenteight.sep.base.testing.BaseJdbcTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.test.context.SpringIntegrationTest;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@JdbcTest
@ComponentScan(basePackageClasses = NotificationModule.class)
@SpringIntegrationTest
class NotificationSchedulerTest extends BaseJdbcTest {

  @Autowired
  private FindNotificationsUseCase findNotificationsUseCase;

  @MockBean
  private EmailSenderUseCase emailSenderUseCase;

  @Autowired
  private ProcessSendingEmails processSendingEmails;

  @Test
  @Sql(scripts = "populate_notifications.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "truncate_notifications.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void processSendingEmails_sendEmailThrowsAnError_NotificationsHaveErrorStatus() {

    doThrow(MailSendException.class)
        .when(emailSenderUseCase)
        .sendEmail(any(), any(), any(), any(), any());

    processSendingEmails.processSendingEmails();

    var cmapiProcessingErrorNotifications =
        findNotificationsUseCase.findNotificationsByTypeAndStatus(
            "CMAPI_PROCESSING_ERROR",
            NotificationStatus.ERROR);

    var csvProcessedNotifications = findNotificationsUseCase.findNotificationsByTypeAndStatus(
        "CSV_PROCESSED",
        NotificationStatus.ERROR);

    assertEquals(3, cmapiProcessingErrorNotifications.size());
    assertEquals(2, csvProcessedNotifications.size());
  }

  @Test
  @Sql(scripts = "populate_notifications.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "truncate_notifications.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
  public void processSendingEmails_sendEmailThrowsNoError_NotificationsHaveSentStatus() {

    doNothing().when(emailSenderUseCase).sendEmail(any(), any(), any(), any(), any());

    processSendingEmails.processSendingEmails();

    var cmapiProcessingErrorNotifications =
        findNotificationsUseCase.findNotificationsByTypeAndStatus(
            "CMAPI_PROCESSING_ERROR",
            NotificationStatus.SENT);

    var csvProcessedNotifications = findNotificationsUseCase.findNotificationsByTypeAndStatus(
        "CSV_PROCESSED",
        NotificationStatus.SENT);

    assertEquals(3, cmapiProcessingErrorNotifications.size());
    assertEquals(2, csvProcessedNotifications.size());
  }
}
