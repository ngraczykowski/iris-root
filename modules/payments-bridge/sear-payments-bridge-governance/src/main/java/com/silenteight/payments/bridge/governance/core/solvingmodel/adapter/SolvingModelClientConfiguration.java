package com.silenteight.payments.bridge.governance.core.solvingmodel.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.model.api.v1.SolvingModelServiceGrpc;
import com.silenteight.payments.bridge.governance.core.solvingmodel.port.CurrentModelClientPort;

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
  CurrentModelClientPort solvingModelClient() {
    var stub = SolvingModelServiceGrpc
        .newBlockingStub(solvingModelChannel)
        .withWaitForReady();
    return new SolvingModelClientClient(stub, properties.getTimeout());
  }
}
