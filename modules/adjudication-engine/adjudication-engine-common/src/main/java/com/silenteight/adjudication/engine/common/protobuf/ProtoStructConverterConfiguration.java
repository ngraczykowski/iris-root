package com.silenteight.adjudication.engine.common.protobuf;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class ProtoStructConverterConfiguration {

  @Bean
  ProtoStructConverter protoStructConverter() {
    return new ProtoStructConverter();
  }
}
