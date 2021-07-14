package com.silenteight.hsbc.bridge.model.transfer;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.BridgeApiProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(BridgeApiProperties.class)
@RequiredArgsConstructor
class ExportModelJobsConfiguration {

  private final BridgeApiProperties bridgeApiProperties;

  @Bean
  ExportModelJobs exportModelJobs(
      ModelClient jenkinsModelClient, GetModelUseCase getModelUseCase) {
    return new ExportModelJobs(
        bridgeApiProperties.getAddress(),
        jenkinsModelClient,
        getModelUseCase);
  }
}
