package com.silenteight.scb.ingest.adapter.incomming.common.logging;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
class LoggingConfiguration implements WebMvcConfigurer {

  private final LoggingInterceptor loggingInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(loggingInterceptor)
        .addPathPatterns("/**");
  }
}
