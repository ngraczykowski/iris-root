package com.silenteight.payments.bridge.ae.recommendation.adapter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.adjudication.api.v2.RecommendationServiceGrpc;
import com.silenteight.payments.bridge.ae.recommendation.port.RecommendationClientPort;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(RecommendationClientProperties.class)
class RecommendationClientConfiguration {

  private final RecommendationClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("ae"))
  private Channel recommendationChannel;

  @Bean
  RecommendationClientPort recommendationClient() {
    var stub = RecommendationServiceGrpc
        .newBlockingStub(recommendationChannel)
        .withWaitForReady();
    return new RecommendationClient(stub, properties.getTimeout());
  }
}
