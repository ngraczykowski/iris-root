package com.silenteight.universaldatasource.common.protobuf;

import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.protocol.MessageRegistry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProtoMessageToObjectNodeConverterConfiguration {

  private final MessageRegistry messageRegistry;

  @Bean
  ProtoMessageToObjectNodeConverter protoMessageToObjectNodeConverter() {
    return new ProtoMessageToObjectNodeConverter(messageRegistry);
  }
}
