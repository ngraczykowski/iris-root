package com.silenteight.payments.bridge.ae.alertregistration.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.adjudication.api.v1.AnalysisServiceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AnalysisClientProperties.class)
class AnalysisClientConfiguration {

  private final AnalysisClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("ae"))
  private Channel analysisServiceChannel;

  @Bean
  AnalysisClient analysisClient() {
    var stub = AnalysisServiceGrpc
        .newBlockingStub(analysisServiceChannel)
        .withWaitForReady();
    return new AnalysisClient(stub, properties.getTimeout());
  }
}
