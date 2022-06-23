package com.silenteight.serp.governance.policy.solve.grpc;

import com.silenteight.serp.governance.policy.solve.SolveUseCase;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PolicyStepsGrpcConfiguration {

  @Bean
  @GrpcService
  PolicyStepsGrpcService policyStepsGrpcService(SolveUseCase solveUseCase) {
    return new PolicyStepsGrpcService(solveUseCase);
  }
}
