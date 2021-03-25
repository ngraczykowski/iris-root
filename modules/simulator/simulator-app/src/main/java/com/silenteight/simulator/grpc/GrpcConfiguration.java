package com.silenteight.simulator.grpc;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfiguration {

  @Bean("adjudication-engine")
  @GrpcClient("adjudication-engine")
  Channel adjudicationEngineChannel(Channel adjudicationEngineChannel) {
    return adjudicationEngineChannel;
  }
}
