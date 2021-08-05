package com.silenteight.hsbc.bridge.unpacker;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(UnpackerProperties.class)
class UnpackerConfiguration {

  private final UnpackerProperties properties;

  @Bean
  FileManager fileUnzipper() {
    return new FileManager(properties.getPath());
  }
}
