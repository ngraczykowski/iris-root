package com.silenteight.serp.governance.model.defaultmodel.grpc;

import com.silenteight.serp.governance.model.domain.ModelQuery;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ModelGrpcConfiguration {

  @Bean
  @GrpcService
  ModelGrpcService modelGrpcService(ModelQuery modelQuery,
      SolvingModelProvider solvingModelProvider) {
    return new ModelGrpcService(modelQuery, solvingModelProvider);
  }
}
