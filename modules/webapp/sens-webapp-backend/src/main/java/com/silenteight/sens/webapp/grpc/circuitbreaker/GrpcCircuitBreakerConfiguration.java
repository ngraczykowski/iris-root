package com.silenteight.sens.webapp.grpc.circuitbreaker;

import com.silenteight.proto.serp.v1.api.DiscrepancyCircuitBreakerGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcCircuitBreakerConfiguration {

  @Bean
  GrpcDiscrepancyCircuitBreakerQuery grpcDiscrepancyCircuitBreakerQuery(
      @Qualifier("circuit-breaker") Channel channel) {
    return new GrpcDiscrepancyCircuitBreakerQuery(
        DiscrepancyCircuitBreakerGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
