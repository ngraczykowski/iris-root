package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcReasoningBranchConfiguration {

  private BranchSolutionMapper branchSolutionMapper = new BranchSolutionMapper();

  @Bean
  GrpcReasoningBranchDetailsQuery grpcReasoningBranchDetailsQuery(
      @Qualifier("governance") Channel channel) {
    return new GrpcReasoningBranchDetailsQuery(
        branchSolutionMapper,
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }

  @Bean
  GrpcReasoningBranchesQuery grpcReasoningBranchesQuery(@Qualifier("governance") Channel channel) {
    return new GrpcReasoningBranchesQuery(
        branchSolutionMapper,
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }

  @Bean
  GrpcReasoningBranchUpdateRepository grpcReasoningBranchUpdateRepository(
      @Qualifier("governance") Channel channel) {
    return new GrpcReasoningBranchUpdateRepository(
        branchSolutionMapper,
        BranchGovernanceGrpc.newBlockingStub(channel).withWaitForReady());
  }
}
