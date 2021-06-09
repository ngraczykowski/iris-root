package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class GovernancePolicyStepsApiClientConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel governanceChannel;

  @Bean
  GovernancePolicyStepsApiClient governancePolicyStepsApiClient() {
    var stub = PolicyStepsSolvingGrpc.newBlockingStub(governanceChannel);
    return new GovernancePolicyStepsApiClient(stub);
  }
}
