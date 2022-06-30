package com.silenteight.agent.autoconfigure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.agent.autoconfigure.grpc.metadata.GrpcMetadataEnhancerConfigurer;
import com.silenteight.agent.autoconfigure.grpc.metadata.SpringBasedAgentInformationProvider;

import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.util.NettyRuntime;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Configuration
@ConditionalOnClass(name = "net.devh.boot.grpc.server.serverfactory.NettyGrpcServerFactory")
@EnableConfigurationProperties(GrpcConfigurationProperties.class)
@RequiredArgsConstructor
@Slf4j
public class GrpcConfiguration {

  private static final String AGENT_NAME_FIELD = "agent";
  private static final String AGENT_VERSION_FIELD = "version";

  private final BuildProperties buildProperties;
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

      log.info("Using executor service with {} threads for gRPC handling.", threads);
      nettyServerBuilder.executor(newFixedThreadPool(threads));
    }
  }

  @Bean
  public GrpcServerConfigurer metadataEnhancingConfigurer() {
    var agentInformationProvider = new SpringBasedAgentInformationProvider(buildProperties);

    return new GrpcMetadataEnhancerConfigurer(
        agentInformationProvider, AGENT_NAME_FIELD, AGENT_VERSION_FIELD).getGrpcServerConfigurer();
  }
}
