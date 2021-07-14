package com.silenteight.simulator.processing.alert.index.grpc;

import com.silenteight.adjudication.api.v1.AlertServiceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcAlertIndexConfiguration {

  @Bean
  GrpcAlertService grpcAlertService(@Qualifier("adjudication-engine") Channel channel) {
    return new GrpcAlertService(
        AlertServiceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
