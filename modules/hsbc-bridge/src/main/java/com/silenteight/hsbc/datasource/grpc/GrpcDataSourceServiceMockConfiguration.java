package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@Profile("!dev") // Change to @Profile(dev) when watchlistService will be implemented !!!
class GrpcDataSourceServiceMockConfiguration {

  @Bean
  NameInformationServiceClient nameInformationServiceApiMock() {
    return new NameInformationServiceClientMock();
  }
}
