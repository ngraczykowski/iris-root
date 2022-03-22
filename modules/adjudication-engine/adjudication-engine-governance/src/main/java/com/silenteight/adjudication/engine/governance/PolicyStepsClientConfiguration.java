package com.silenteight.adjudication.engine.governance;

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
@EnableConfigurationProperties(PolicyStepsClientProperties.class)
class PolicyStepsClientConfiguration {

  @Valid
  private final PolicyStepsClientProperties properties;

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel governanceChannel;

  @Bean
  PolicyStepsClient governancePolicyStepsApiClient() {
    var stub = PolicyStepsSolvingGrpc
        .newBlockingStub(governanceChannel)
        .withWaitForReady();
    return new PolicyStepsClient(stub, properties.getTimeout());
  }
}
