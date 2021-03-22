package com.silenteight.hsbc.bridge.adjudication;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class DatasetConfiguration {

  @Bean
  DatasetService datasetService(DatasetServiceBlockingStub datasetServiceBlockingStub) {
    return new DatasetService(datasetServiceBlockingStub);
  }

}
