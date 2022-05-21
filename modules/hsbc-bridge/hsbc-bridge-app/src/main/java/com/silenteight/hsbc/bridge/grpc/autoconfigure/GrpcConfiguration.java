package com.silenteight.hsbc.bridge.grpc.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.util.NettyRuntime;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

@Configuration
@ConditionalOnClass(name = "net.devh.boot.grpc.server.serverfactory.NettyGrpcServerFactory")
@EnableConfigurationProperties(GrpcConfigurationProperties.class)
@RequiredArgsConstructor
@Slf4j
public class GrpcConfiguration {

  private final GrpcConfigurationProperties properties;

  @Bean
  public GrpcServerConfigurer threadBoundingConfigurer() {
    return this::configureThreads;
  }

  private void configureThreads(ServerBuilder<?> serverBuilder) {
    if (serverBuilder instanceof NettyServerBuilder) {
      var nettyServerBuilder = (NettyServerBuilder) serverBuilder;
      var threads = properties.getThreads()
          .filter(t -> t > 0)
          .orElse(NettyRuntime.availableProcessors());

      log.info("Using executor service with " + threads + " threads for gRPC handling.");
      nettyServerBuilder.executor(Executors.newFixedThreadPool(threads));
    }
  }
}
