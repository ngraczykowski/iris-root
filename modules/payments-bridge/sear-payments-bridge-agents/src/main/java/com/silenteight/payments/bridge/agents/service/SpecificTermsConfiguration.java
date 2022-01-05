package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SpecificTermsProperties.class)
class SpecificTermsConfiguration {

  private static final Pattern COMPILE = Pattern.compile(",,");
  private final SpecificTermsProperties properties;

  @Bean
  SpecificTermsUseCase specificTermsAgent() {
    return new SpecificTermsAgent(
        List.of(COMPILE.split(properties.getRegularTerms())));
  }
}
