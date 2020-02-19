package com.silenteight.sens.webapp.grpc;

import lombok.Setter;

import io.grpc.Channel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GrpcConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel channel;

  @Bean("governance")
  @GrpcClient("governance")
  Channel governanceChannel(Channel channel) {
    return channel;
  }
}
