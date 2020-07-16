package com.silenteight.sens.webapp.grpc.configuration.solution;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfigurationSolutionConfiguration {

  @Bean
  GrpcSolutionsQuery grpcSolutionsQuery(@Qualifier("governance") Channel channel) {
    return new GrpcSolutionsQuery(
        BranchSolutionGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
