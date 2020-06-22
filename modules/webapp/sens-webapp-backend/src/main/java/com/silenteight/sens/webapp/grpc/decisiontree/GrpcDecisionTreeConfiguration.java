package com.silenteight.sens.webapp.grpc.decisiontree;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcDecisionTreeConfiguration {

  @Bean
  GrpcDecisionTreeQuery grpcDecisionTreeQuery(
      @Qualifier("governance") Channel channel) {
    return new GrpcDecisionTreeQuery(
        DecisionTreeGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
