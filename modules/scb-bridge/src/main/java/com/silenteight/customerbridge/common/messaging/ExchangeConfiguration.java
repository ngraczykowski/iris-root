package com.silenteight.customerbridge.common.messaging;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.customerbridge.common.messaging.MessagingConstants.EXCHANGE_ALERT_UNPROCESSED;

@Configuration
public class ExchangeConfiguration {

  @Bean
  FanoutExchange alertUnprocessed() {
    return ExchangeBuilder
        .fanoutExchange(EXCHANGE_ALERT_UNPROCESSED)
        .build();
  }
}
