package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.solving.api.v1.PolicyStepsSolvingGrpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GovernanceClientProperties.class)
class GovernancePolicyStepsApiClientConfiguration {

  @Valid
  private final GovernanceClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel governanceChannel;

  @Bean
  GovernancePolicyStepsApiClient governancePolicyStepsApiClient() {
    var stub = PolicyStepsSolvingGrpc.newBlockingStub(governanceChannel);
    return new GovernancePolicyStepsApiClient(stub, properties.getTimeout());
  }
}
