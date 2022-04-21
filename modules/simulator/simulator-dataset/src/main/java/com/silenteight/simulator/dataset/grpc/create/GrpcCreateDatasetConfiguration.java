package com.silenteight.simulator.dataset.grpc.create;

import lombok.Setter;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcCreateDatasetConfiguration {

  @Setter(onMethod_ = @GrpcClient("adjudicationengine"))
  private Channel adjudicationEngineChannel;

  @Bean
  GrpcCreateDatasetService grpcCreateDatasetService() {
    return new GrpcCreateDatasetService(
        DatasetServiceGrpc.newBlockingStub(adjudicationEngineChannel).withWaitForReady());
  }
}
