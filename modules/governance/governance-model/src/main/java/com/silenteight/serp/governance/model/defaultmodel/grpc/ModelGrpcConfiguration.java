package com.silenteight.serp.governance.model.defaultmodel.grpc;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelGrpcConfiguration {

  @Bean
  @GrpcService
  ModelGrpcService modelGrpcService(DefaultModelQuery defaultModelQuery) {
    return new ModelGrpcService(defaultModelQuery);
  }
}
