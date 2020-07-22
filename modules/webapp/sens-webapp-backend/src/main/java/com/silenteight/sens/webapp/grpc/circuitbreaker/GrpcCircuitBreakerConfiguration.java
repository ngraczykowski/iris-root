package com.silenteight.sens.webapp.grpc.circuitbreaker;

import com.silenteight.proto.serp.v1.api.DiscrepancyCircuitBreakerGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CircuitBreakerProperties.class)
class GrpcCircuitBreakerConfiguration {

  @Bean
  GrpcDiscrepancyCircuitBreakerQuery grpcDiscrepancyCircuitBreakerQuery(
      @Qualifier("circuit-breaker") Channel channel,
      CircuitBreakerProperties config) {
    return new GrpcDiscrepancyCircuitBreakerQuery(
        DiscrepancyCircuitBreakerGrpc
            .newBlockingStub(channel)
            .withWaitForReady(),
        config.getLimitArchivedDiscrepantBranches());
  }
}
