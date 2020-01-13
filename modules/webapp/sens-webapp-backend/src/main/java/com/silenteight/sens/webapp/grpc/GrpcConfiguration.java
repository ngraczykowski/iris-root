package com.silenteight.sens.webapp.grpc;

import lombok.Setter;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel channel;

  @Bean
  GrpcDecisionTreeRepository grpcDecisionTreeRepository() {
    return new GrpcDecisionTreeRepository(
        DecisionTreeGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
