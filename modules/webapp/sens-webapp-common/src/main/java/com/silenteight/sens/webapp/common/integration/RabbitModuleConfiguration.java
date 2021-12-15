package com.silenteight.sens.webapp.common.integration;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class RabbitModuleConfiguration {

  @Bean
  @Primary
  MessageRegistry messageRegistryOverwrite() {
    MessageRegistryFactory factory = new MessageRegistryFactory(
        "com.silenteight.audit.bs.api",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    );

    return factory.create();
  }
}
