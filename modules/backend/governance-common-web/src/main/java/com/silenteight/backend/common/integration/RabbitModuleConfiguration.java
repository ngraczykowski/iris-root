/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.common.integration;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitModuleConfiguration {

  @Bean
  @Primary
  MessageRegistry messageRegistryOverwrite() {
    MessageRegistryFactory factory = new MessageRegistryFactory(
        "com.silenteight.model.api",
        "com.silenteight.adjudication.api",
        "com.silenteight.solving.api",
        "com.silenteight.data.api",
        "com.silenteight.dataretention.api",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type");

    return factory.create();
  }
}
