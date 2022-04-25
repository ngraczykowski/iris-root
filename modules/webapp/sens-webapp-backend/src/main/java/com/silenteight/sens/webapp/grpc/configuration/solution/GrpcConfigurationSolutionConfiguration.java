package com.silenteight.sens.webapp.grpc.configuration.solution;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.BranchSolutionBlockingStub;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfigurationSolutionConfiguration {

  @GrpcClient("governance")
  private BranchSolutionBlockingStub branchSolutionBlockingStub;

  @Bean
  GrpcSolutionsQuery grpcSolutionsQuery() {
    return new GrpcSolutionsQuery(branchSolutionBlockingStub.withWaitForReady());
  }
}
