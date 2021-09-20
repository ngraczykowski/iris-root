package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;


import com.silenteight.payments.bridge.svb.learning.notification.model.NotificationRequest;
import com.silenteight.payments.bridge.svb.learning.notification.port.outgoing.AmazonSesClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendNotificationAdapterTest {

  @Mock
  private AmazonSesClient amazonSesClient;
  private SendNotificationService sendNotificationService;

  @BeforeEach
  void setUp() {
    sendNotificationService =
        new SendNotificationService(amazonSesClient, new SendNotificationProperties());
  }

  @Test
  void shouldSend() {
    sendNotificationService.sendNotification(NotificationRequest
        .builder()
        .successfullyRecords(2137)
        .failedRecord(7)
        .build());
    verify(amazonSesClient).sendEmail(any());
  }
}
