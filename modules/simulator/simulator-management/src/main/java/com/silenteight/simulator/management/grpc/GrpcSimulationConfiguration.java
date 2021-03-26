package com.silenteight.simulator.management.grpc;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcSimulationConfiguration {

  @Bean
  GrpcModelService grpcModelService(
      @Qualifier("governance") Channel channel) {

    return new GrpcModelService(
        SolvingModelServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }

  @Bean
  GrpcAnalysisService grpcAnalysisService(
      @Qualifier("adjudication-engine") Channel channel) {

    return new GrpcAnalysisService(
        AnalysisServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
