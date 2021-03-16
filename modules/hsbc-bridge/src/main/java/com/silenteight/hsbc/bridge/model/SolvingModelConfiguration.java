package com.silenteight.hsbc.bridge.model;

import lombok.RequiredArgsConstructor;

import com.silenteight.model.api.v1.SolvingModelServiceGrpc;
import com.silenteight.model.api.v1.SolvingModelServiceGrpc.SolvingModelServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
class SolvingModelConfiguration {

  private final SolvingModelGrpcProperties solvingModelGrpcProperties;

  @Bean
  GetDefaultModelUseCase getDefaultModelUseCase() {
    return new GetDefaultModelUseCase(getStub());
  }

  private SolvingModelServiceBlockingStub getStub() {
    return SolvingModelServiceGrpc.newBlockingStub(createChannel())
        .withWaitForReady();
  }

  private ManagedChannel createChannel() {
    return ManagedChannelBuilder.forTarget(solvingModelGrpcProperties.getAddress())
        .usePlaintext()
        .build();
  }
}
