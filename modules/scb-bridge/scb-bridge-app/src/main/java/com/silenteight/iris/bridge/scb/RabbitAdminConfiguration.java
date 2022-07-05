/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitAdminConfiguration {

  private static final int DECLARE_MAX_ATTEMPTS = 5;
  private static final int DECLARE_INITIAL_RETRY_INTERVAL = 5000;
  private static final double DECLARE_RETRY_MULTIPLIER = 3.0;
  private static final int DECLARE_MAX_RETRY_INTERVAL = 60000;

  @Bean
  AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
    rabbitAdmin.setRetryTemplate(retryTemplate());
    return rabbitAdmin;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();
    retryTemplate.setBackOffPolicy(exponentialBackOffPolicy());
    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(DECLARE_MAX_ATTEMPTS);
    retryTemplate.setRetryPolicy(retryPolicy);
    return retryTemplate;
  }

  private ExponentialBackOffPolicy exponentialBackOffPolicy() {
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(DECLARE_INITIAL_RETRY_INTERVAL);
    backOffPolicy.setMultiplier(DECLARE_RETRY_MULTIPLIER);
    backOffPolicy.setMaxInterval(DECLARE_MAX_RETRY_INTERVAL);
    return backOffPolicy;
  }
}
