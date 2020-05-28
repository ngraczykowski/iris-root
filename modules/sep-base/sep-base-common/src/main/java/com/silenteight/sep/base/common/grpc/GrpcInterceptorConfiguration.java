package com.silenteight.sep.base.common.grpc;

import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorRegistry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static java.util.stream.Collectors.toList;

@Configuration
@ConditionalOnClass(GlobalServerInterceptorRegistry.class)
public class GrpcInterceptorConfiguration {

  @Bean
  StatusRuntimeGrpcExceptionHandler statusRuntimeGrpcExceptionHandler() {
    return new StatusRuntimeGrpcExceptionHandler();
  }

  @Bean
  EntityNotFoundGrpcExceptionHandler entityNotFoundGrpcExceptionHandler() {
    return new EntityNotFoundGrpcExceptionHandler();
  }

  @Bean
  GlobalExceptionHandlingServerInterceptor globalExceptionHandlingServerInterceptor(
      ObjectProvider<GrpcExceptionHandler> exceptionHandlersProvider) {

    var handlers = exceptionHandlersProvider.stream().collect(toList());
    return new GlobalExceptionHandlingServerInterceptor(handlers);
  }
}
