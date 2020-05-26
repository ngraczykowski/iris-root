package com.silenteight.sens.webapp.grpc.bulkchange;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeGovernanceGrpc;
import com.silenteight.sens.webapp.grpc.BranchSolutionMapper;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcBulkChangeConfiguration {

  @Bean
  GrpcBulkChangeQuery grpcBulkChangeQuery(@Qualifier("governance") Channel channel) {
    return new GrpcBulkChangeQuery(
        new BranchSolutionMapper(),
        BulkBranchChangeGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
