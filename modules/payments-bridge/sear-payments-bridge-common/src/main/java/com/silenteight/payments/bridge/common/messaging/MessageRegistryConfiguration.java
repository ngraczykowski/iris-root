package com.silenteight.payments.bridge.common.messaging;

import com.silenteight.sep.base.common.protocol.MessageRegistry;
import com.silenteight.sep.base.common.protocol.MessageRegistryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MessageRegistryConfiguration {

  @Bean
  @Primary
  MessageRegistry messageRegistryOverwrite() {
    MessageRegistryFactory factory = new MessageRegistryFactory(
        "com.silenteight.adjudication.engine",
        "com.silenteight.datasource",
        "com.silenteight.agents",
        "com.google.protobuf",
        "com.google.rpc",
        "com.google.type"
    );

    return factory.create();
  }
}
