package com.silenteight.serp.governance.strategy.solve.grpc;

import com.silenteight.serp.governance.strategy.solve.SolveAlertUseCase;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class AlertsGrpcConfiguration {

  @Bean
  @GrpcService
  AlertsGrpcService alertsGrpcService(SolveAlertUseCase solveAlertUseCase) {
    return new AlertsGrpcService(solveAlertUseCase);
  }
}
