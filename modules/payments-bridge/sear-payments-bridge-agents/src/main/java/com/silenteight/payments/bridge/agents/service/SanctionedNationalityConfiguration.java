package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.port.SanctionedNationalityUseCase;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SanctionedNationalityProperties.class)
class SanctionedNationalityConfiguration {

  private static final Pattern COMPILE = Pattern.compile(",,");
  private final SanctionedNationalityProperties properties;

  @Bean
  SanctionedNationalityUseCase sanctionedNationalityAgent() {
    return new SanctionedNationalityAgent(
        List.of(COMPILE.split(properties.getSanctionedNationalities())));
  }
}
