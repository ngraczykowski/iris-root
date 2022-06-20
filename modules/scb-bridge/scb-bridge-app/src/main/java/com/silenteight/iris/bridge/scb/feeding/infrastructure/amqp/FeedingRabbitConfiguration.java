/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.infrastructure.amqp;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AmqpFeedingOutgoingMatchFeatureInputSetFedProperties.class)
public class FeedingRabbitConfiguration {

  @Bean
  DirectExchange matchFeatureInputSetFedExchange(
      AmqpFeedingOutgoingMatchFeatureInputSetFedProperties properties) {
    return new DirectExchange(properties.exchangeName());
  }
}
