package com.silenteight.adjudication.engine.common.grpc;

import com.silenteight.sep.base.common.grpc.GrpcInterceptorConfiguration;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(GrpcInterceptorConfiguration.class)
public class GrpcExceptionsConfiguration {

  @Bean
  ValidationGrpcExceptionHandler validationGrpcExceptionHandler() {
    return new ValidationGrpcExceptionHandler();
  }
}
