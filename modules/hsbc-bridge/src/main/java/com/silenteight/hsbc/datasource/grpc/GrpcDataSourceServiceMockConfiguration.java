package com.silenteight.hsbc.datasource.grpc;

import com.silenteight.hsbc.datasource.extractors.ispep.IsPepServiceClient;
import com.silenteight.hsbc.datasource.extractors.name.NameInformationServiceClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
class GrpcDataSourceServiceMockConfiguration {

  @Bean
  IsPepServiceClient isPepInformationServiceApiMock() {
    return new IsPepServiceClientMock();
  }

  @Bean
  NameInformationServiceClient nameInformationServiceApiMock() {
    return new NameInformationServiceClientMock();
  }
}
