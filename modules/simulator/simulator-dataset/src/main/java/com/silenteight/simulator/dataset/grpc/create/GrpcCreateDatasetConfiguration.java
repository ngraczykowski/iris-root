package com.silenteight.simulator.dataset.grpc.create;

import com.silenteight.adjudication.api.v1.DatasetServiceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcCreateDatasetConfiguration {

  @Bean
  GrpcCreateDatasetService grpcCreateDatasetService(
      @Qualifier("adjudication-engine") Channel channel) {

    return new GrpcCreateDatasetService(
        DatasetServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
