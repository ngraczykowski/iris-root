package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "pb.learning.notification")
@Validated
@Data
class SendNotificationProperties {

  private static final String DEFAULT_SENDER = "Silent Eight <wkeska@silenteight.com>";

  private static final String DEFAULT_RECIPIENT = "wkeska+test@silenteight.com";

  private static final String DEFAULT_SUBJECT = "Learning data report";

  private String sender = DEFAULT_SENDER;

  private String recipient = DEFAULT_RECIPIENT;

  private String subject = DEFAULT_SUBJECT;
}
