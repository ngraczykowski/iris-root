package com.silenteight.sens.webapp.backend.application.grpc.governance;

import lombok.Setter;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcGovernanceConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel channel;

  @Bean
  GrpcBranchGovernanceClient grpcBranchGovernanceClient() {
    return new GrpcBranchGovernanceClient(
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
