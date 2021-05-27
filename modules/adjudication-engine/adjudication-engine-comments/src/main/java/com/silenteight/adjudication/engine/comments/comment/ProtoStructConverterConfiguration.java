package com.silenteight.adjudication.engine.comments.comment;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.common.protobuf.ProtoStructConverter;

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
