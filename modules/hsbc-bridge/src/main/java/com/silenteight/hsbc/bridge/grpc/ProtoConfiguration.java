package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.amqp.MessageRegistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProtoConfiguration {

  @Bean
  MessageRegistry protoMessageRegistry() {
    return new ProtoMessageRegistry();
  }
}
