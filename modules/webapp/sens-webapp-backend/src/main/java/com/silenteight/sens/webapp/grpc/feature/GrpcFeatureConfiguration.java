package com.silenteight.sens.webapp.grpc.feature;

import com.silenteight.proto.serp.v1.api.FeatureGovernanceGrpc;

import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcFeatureConfiguration {

  @Bean
  GrpcFeatureQuery grpcFeatureQuery(
      @Qualifier("governance") Channel channel) {
    return new GrpcFeatureQuery(
        FeatureGovernanceGrpc
            .newBlockingStub(channel)
            .withWaitForReady());
  }
}
