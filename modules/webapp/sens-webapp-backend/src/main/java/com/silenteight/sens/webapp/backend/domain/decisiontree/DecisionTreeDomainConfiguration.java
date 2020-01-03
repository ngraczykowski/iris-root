package com.silenteight.sens.webapp.backend.domain.decisiontree;

import lombok.Setter;

import com.silenteight.proto.serp.v1.api.DecisionTreeGovernanceGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DecisionTreeDomainConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel channel;

  @Bean
  DecisionTreeService decisionTreeService(DecisionTreeRepository repository) {
    return new DecisionTreeService(repository);
  }

  @Bean
  DecisionTreeRepository decisionTreeRepository() {
    return new GrpcDecisionTreeRepository(
        DecisionTreeGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
