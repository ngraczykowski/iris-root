/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.configserver.dynamic.generator;

import lombok.RequiredArgsConstructor;

import com.silenteight.configserver.IrisRabbitProperties;
import com.silenteight.configserver.dynamic.IrisDynamicPropertiesGenerator;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Map.entry;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(IrisRabbitProperties.class)
class Rabbit implements IrisDynamicPropertiesGenerator {

  private final IrisRabbitProperties irisRabbitProperties;

  @Override
  public Map<String, Object> generate(String application, String profile, String label) {

    return Map.ofEntries(
        entry(
            "spring.rabbitmq.addresses",
            "amqp://"
                + irisRabbitProperties.getHost()
                + ":"
                + irisRabbitProperties.getPort()
                + irisRabbitProperties.getVirtualHost()),
        entry("spring.rabbitmq.username", irisRabbitProperties.getUsername()),
        entry("spring.rabbitmq.password", irisRabbitProperties.getPassword()));
  }
}
