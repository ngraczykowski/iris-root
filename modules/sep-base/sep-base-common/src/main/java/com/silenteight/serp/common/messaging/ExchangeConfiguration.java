package com.silenteight.serp.common.messaging;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.serp.common.messaging.MessagingConstants.*;

@Configuration
@SuppressWarnings("squid:S138")
class ExchangeConfiguration {

  private static final String ALTERNATE_EXCHANGE_ARG = "alternate-exchange";

  @Bean
  FanoutExchange alertUnprocessed() {
    return (FanoutExchange) ExchangeBuilder
        .fanoutExchange(EXCHANGE_ALERT_UNPROCESSED)
        .build();
  }

  @Bean
  FanoutExchange alertProcessed() {
    return (FanoutExchange) ExchangeBuilder
        .fanoutExchange(EXCHANGE_ALERT_PROCESSED)
        .build();
  }

  @Bean
  DirectExchange alertOrders() {
    return (DirectExchange) ExchangeBuilder
        .directExchange(EXCHANGE_ALERT_ORDERS)
        .build();
  }

  @Bean
  DirectExchange alertMetadata() {
    return (DirectExchange) ExchangeBuilder
        .directExchange(EXCHANGE_ALERT_METADATA)
        .build();
  }

  @Bean
  FanoutExchange alertNotRoutedExchange() {
    return (FanoutExchange) ExchangeBuilder
        .fanoutExchange(EXCHANGE_ALERT_NOT_ROUTED)
        .internal()
        .build();
  }

  @Bean
  FanoutExchange alertIdExchange() {
    return (FanoutExchange) ExchangeBuilder
        .fanoutExchange(EXCHANGE_ALERT_ID)
        .build();
  }

  @Bean
  DirectExchange alertRecommendationExchange(FanoutExchange alertNotRoutedExchange) {
    return (DirectExchange) ExchangeBuilder
        .directExchange(EXCHANGE_ALERT_RECOMMENDATION)
        .withArgument(ALTERNATE_EXCHANGE_ARG, alertNotRoutedExchange.getName())
        .build();
  }

  @Bean
  TopicExchange reportData() {
    return (TopicExchange) ExchangeBuilder
        .topicExchange(EXCHANGE_REPORT_DATA)
        .build();
  }

  @Bean
  TopicExchange learningExchange() {
    return (TopicExchange) ExchangeBuilder
        .topicExchange(EXCHANGE_LEARNING)
        .build();
  }

  @Bean
  TopicExchange notificationExchange() {
    return (TopicExchange) ExchangeBuilder
        .topicExchange(EXCHANGE_NOTIFICATION)
        .build();
  }
}
