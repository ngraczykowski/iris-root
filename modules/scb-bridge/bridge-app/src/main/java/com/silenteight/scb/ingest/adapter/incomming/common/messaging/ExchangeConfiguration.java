package com.silenteight.scb.ingest.adapter.incomming.common.messaging;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExchangeConfiguration {

  @Bean
  FanoutExchange alertUnprocessed() {
    return ExchangeBuilder
        .fanoutExchange(MessagingConstants.EXCHANGE_ALERT_UNPROCESSED)
        .build();
  }
}
