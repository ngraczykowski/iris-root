package com.silenteight.sens.webapp.grpc;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import io.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GrpcConfigurationProperties.class)
class GrpcConfiguration {

  @Setter(onMethod_ = @GrpcClient("governance"))
  private Channel channel;

  @Bean("governance")
  @GrpcClient("governance")
  Channel governanceChannel(Channel channel) {
    return channel;
  }

  @SuppressWarnings({ "unused", "squid:S3985" })
  @RequiredArgsConstructor
  @GrpcGlobalClientInterceptor
  private static class TimeoutInterceptor implements ClientInterceptor {

    private final GrpcTimeoutConfig config;

    @Override
    @SuppressWarnings("squid:S00119")
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
        MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
      return next.newCall(method, callOptions.withDeadline(config.getDeadline()));
    }
  }
}
