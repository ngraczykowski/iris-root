/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.mailer;

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
