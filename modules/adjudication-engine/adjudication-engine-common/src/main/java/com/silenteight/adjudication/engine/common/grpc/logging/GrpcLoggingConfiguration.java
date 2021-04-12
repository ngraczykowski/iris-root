package com.silenteight.adjudication.engine.common.grpc.logging;

import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({
    GlobalServerInterceptorConfigurer.class,
})
@EnableConfigurationProperties(GrpcLoggingProperties.class)
public class GrpcLoggingConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "grpc.logging", name = "enabled", havingValue = "true")
  LoggingServerInterceptorConfigurer loggingServerInterceptorConfigurer() {
    return new LoggingServerInterceptorConfigurer(new LoggingServerInterceptor());
  }
}
