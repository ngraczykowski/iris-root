package com.silenteight.payments.bridge.firco.alertmessage.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.MessageChannel;

@Configuration
class IntChannels {

  private static final String PREFIX = "alertMessage";

  public static final String INT_ALERT_STORED_ACCEPTED = PREFIX + "IntAlertStoredAccepted";

  @Bean(INT_ALERT_STORED_ACCEPTED)
  MessageChannel alertStored() {
    return new DirectChannel();
  }

}
