package com.silenteight.serp.governance.mailer;

import lombok.Value;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@ConfigurationProperties(prefix = "serp.notifier.mail")
@ConstructorBinding
@Value
class MailNotificationProperties {

  String from;

  List<String> toAddresses;
}
