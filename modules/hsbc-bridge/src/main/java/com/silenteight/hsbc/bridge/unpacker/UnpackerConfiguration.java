package com.silenteight.hsbc.bridge.unpacker;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class UnpackerConfiguration {

  @Bean
  FileUnzipper fileUnzipper() {
    return new FileUnzipper();
  }
}
