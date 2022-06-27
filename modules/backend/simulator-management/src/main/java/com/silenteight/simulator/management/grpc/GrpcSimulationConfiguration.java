package com.silenteight.simulator.management.grpc;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc.AnalysisServiceBlockingStub;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;
import com.silenteight.simulator.management.create.AnalysisService;
import com.silenteight.simulator.management.create.ModelService;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcSimulationConfiguration {

  @GrpcClient("governance")
  private SolvingModelServiceBlockingStub solvingModelServiceBlockingStub;

  @GrpcClient("adjudicationengine")
  private AnalysisServiceBlockingStub analysisServiceBlockingStub;

  @Bean
  ModelService grpcModelService() {
    return new GrpcModelService(solvingModelServiceBlockingStub.withWaitForReady());
  }

  @Bean
  AnalysisService grpcAnalysisService() {
    return new GrpcAnalysisService(analysisServiceBlockingStub.withWaitForReady());
  }
}
