package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcReasoningBranchConfiguration {

  @Bean
  GrpcReasoningBranchDetailsQuery grpcReasoningBranchDetailsQuery(
      @Qualifier("governance") Channel channel) {
    return new GrpcReasoningBranchDetailsQuery(
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady()
    );
  }
}
