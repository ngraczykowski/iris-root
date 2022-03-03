package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class GnsSolutionMapperConfiguration {

  private final GnsSolutionProperties gnsSolutionProperties;

  @Bean
  GnsSolutionMapper gnsSolutionMapper() {
    return new GnsSolutionMapper(gnsSolutionProperties.getStates());
  }
}
