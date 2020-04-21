package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc;
import com.silenteight.sens.webapp.audit.api.AuditLog;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcReasoningBranchConfiguration {

  private BranchSolutionMapper branchSolutionMapper = new BranchSolutionMapper();

  @Bean
  GrpcReasoningBranchDetailsQuery grpcReasoningBranchDetailsQuery(
      @Qualifier("governance") Channel channel, AuditLog auditLog) {
    return new GrpcReasoningBranchDetailsQuery(
        branchSolutionMapper,
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady(),
        auditLog);
  }

  @Bean
  GrpcReasoningBranchesQuery grpcReasoningBranchesQuery(
      @Qualifier("governance") Channel channel, AuditLog auditLog) {
    return new GrpcReasoningBranchesQuery(
        branchSolutionMapper,
        BranchGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady(),
        auditLog);
  }

  @Bean
  GrpcReasoningBranchUpdateRepository grpcReasoningBranchUpdateRepository(
      @Qualifier("governance") Channel channel, AuditLog auditLog) {
    return new GrpcReasoningBranchUpdateRepository(
        branchSolutionMapper,
        BranchGovernanceGrpc.newBlockingStub(channel).withWaitForReady(),
        auditLog);
  }
}
