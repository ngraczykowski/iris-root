package com.silenteight.sens.webapp.backend.application.grpc.reco;

import lombok.Setter;

import com.silenteight.proto.serp.v1.api.RecommendationGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcRecoConfiguration {

  @Setter(onMethod_ = @GrpcClient("reco"))
  private Channel channel;

  @Bean
  GrpcRecoClient grpcRecoClient() {
    return new GrpcRecoClient(
        RecommendationGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
