package com.silenteight.payments.bridge.governance.solvingmodel.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.model.api.v1.SolvingModelServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(SolvingModelClientProperties.class)
class SolvingModelClientConfiguration {

  private final SolvingModelClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel solvingModelChannel;

  @Bean
  SolvingModelServiceClient solvingModelClient() {
    var stub = SolvingModelServiceGrpc
        .newBlockingStub(solvingModelChannel)
        .withWaitForReady();
    return new SolvingModelServiceClient(stub, properties.getTimeout());
  }
}
