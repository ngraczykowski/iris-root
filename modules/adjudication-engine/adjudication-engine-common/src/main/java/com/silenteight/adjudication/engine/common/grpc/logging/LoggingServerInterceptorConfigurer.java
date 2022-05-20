package com.silenteight.adjudication.engine.common.grpc.logging;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;

import java.util.List;

@RequiredArgsConstructor
final class LoggingServerInterceptorConfigurer implements GlobalServerInterceptorConfigurer {

  @NonNull
  private final LoggingServerInterceptor loggingServerInterceptor;

  @Override
  public void configureServerInterceptors(List<ServerInterceptor> interceptors) {
    interceptors.add(0, loggingServerInterceptor);
  }
}
