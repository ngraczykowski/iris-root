package com.silenteight.hsbc.bridge.ispep;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class IsPepConfiguration {

  private final IsPepMessageSender messageSender;

  @Bean
  IsPep isPep() {
    return new IsPep(messageSender);
  }
}
