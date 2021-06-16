package com.silenteight.adjudication.engine.analysis.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.solving.api.v1.AlertsSolvingGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AlertSolvingClientProperties.class)
class AlertSolvingClientConfiguration {

  @Valid
  private final AlertSolvingClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel alertSolvingDataChannel;

  @Bean
  AlertSolvingClient alertSolvingClient() {
    var stub = AlertsSolvingGrpc
        .newBlockingStub(alertSolvingDataChannel)
        .withWaitForReady();

    return new AlertSolvingClient(stub, properties.getTimeout());
  }
}
