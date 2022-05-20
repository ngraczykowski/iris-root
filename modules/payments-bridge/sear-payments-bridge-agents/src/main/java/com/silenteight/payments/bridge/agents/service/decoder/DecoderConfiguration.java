package com.silenteight.payments.bridge.agents.service.decoder;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@Configuration
@RequiredArgsConstructor
class DecoderConfiguration {

  private final ResourceLoader resourceLoaderToDelegate;

  @Bean
  DecodedResourceLoader decodedResourceLoader() {
    return new DecodedResourceLoaderImpl(
        new GunzipDecoder("s8a"), resourceLoaderToDelegate);
  }
}
