package com.silenteight.sens.webapp.grpc;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.proto.serp.v1.governance.BranchSolutionGrpc.BranchSolutionBlockingStub;

import io.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.client.inject.GrpcClientBean;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;

@Configuration
@EnableConfigurationProperties(GrpcConfigurationProperties.class)
class GrpcConfiguration {

  @Setter(onMethod_ = @GrpcClient("circuit-breaker"))
  private Channel circuitBreakerChannel;

  @Bean("circuit-breaker")
  Channel circuitBreakerChannel() {
    return circuitBreakerChannel;
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
