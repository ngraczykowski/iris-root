package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AlertClientProperties.class)
class AlertClientConfiguration {

  private final AlertClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("ae"))
  private Channel alertServiceChannel;

  @Bean
  AlertClient alertClient() {
    var stub = AlertServiceGrpc
        .newBlockingStub(alertServiceChannel)
        .withWaitForReady();
    return new AlertClient(stub, properties.getTimeout());
  }
}
