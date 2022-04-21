package com.silenteight.simulator.management.grpc;

import lombok.Setter;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcSimulationConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel governanceChannel;

  @Setter(onMethod_ = @GrpcClient("adjudicationengine"))
  private Channel adjudicationEngineChannel;

  @Bean
  GrpcModelService grpcModelService() {
    return new GrpcModelService(
        SolvingModelServiceGrpc.newBlockingStub(governanceChannel).withWaitForReady());
  }

  @Bean
  GrpcAnalysisService grpcAnalysisService() {
    return new GrpcAnalysisService(
        AnalysisServiceGrpc.newBlockingStub(adjudicationEngineChannel).withWaitForReady());
  }
}
