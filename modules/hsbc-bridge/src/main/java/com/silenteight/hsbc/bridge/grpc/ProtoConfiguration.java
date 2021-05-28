package com.silenteight.hsbc.bridge.grpc;

import com.silenteight.hsbc.bridge.amqp.MessageRegistry;

import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
class ProtoConfiguration {

  @Bean
  MessageRegistry protoMessageRegistry() {
    return new ProtoMessageRegistry();
  }
}
