package com.silenteight.payments.bridge.notification.service;

import com.silenteight.payments.bridge.notification.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.notification.port.CmapiNotificationCreatorUseCase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest(classes = CmapiNotificationCreatorService.class)
class CmapiNotificationCreatorServiceTest {

  @Autowired
  private CmapiNotificationCreatorUseCase cmapiNotificationCreatorUseCase;

  @Test
  void createCmapiNotification_checkIfCreatedNotificationIsAsExpected() {

    var cmapiNotificationRequest = CmapiNotificationRequest.builder()
        .alertId("b2f13285-220a-d24f-5158-020159ba9058")
        .alertName("alerts/2")
        .messageId("CEC45D14-44C2-D2E9-E053-1A186C0ADD76")
        .systemId("DIN20211020170021-00056-28245")
        .message("First char is not / when hitTag= 50")
        .build();

    var expectedNotification =
        cmapiNotificationCreatorUseCase.createCmapiNotification(cmapiNotificationRequest);

    String expectedMessage = "<html>\n"
        + "<body>\n"
        + "<p>This is to let you know there was an error with alert’s processing.</p>\n"
        + "<p>Please see the details in the file attached.</p>\n"
        + "<br>\n"
        + "<p>Thank you.</p>\n"
        + "<p>Silent Eight</p>\n"
        + "</body>\n"
        + "</html>\n";

    assertEquals(expectedMessage, expectedNotification.getMessage());
    assertNotNull(expectedNotification.getAttachment());
    assertEquals("CMAPI_ERRORS.csv", expectedNotification.getAttachmentName());
    assertEquals("CMAPI_PROCESSING_ERROR", expectedNotification.getType());
    assertNull(expectedNotification.getStatus());

  }
}
