package com.silenteight.serp.governance.mailer;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties(prefix = "serp.notifier.integration.mail")
@ConstructorBinding
@Value
class MailNotificationIntegrationProperties {

  @NotBlank
  String inboundQueueName;
}
