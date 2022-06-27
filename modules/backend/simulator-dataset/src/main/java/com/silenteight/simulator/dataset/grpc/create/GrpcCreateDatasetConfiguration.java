package com.silenteight.simulator.dataset.grpc.create;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcCreateDatasetConfiguration {

  @GrpcClient("adjudicationengine")
  private DatasetServiceBlockingStub datasetServiceBlockingStub;

  @Bean
  GrpcCreateDatasetService grpcCreateDatasetService() {
    return new GrpcCreateDatasetService(datasetServiceBlockingStub.withWaitForReady());
  }
}
