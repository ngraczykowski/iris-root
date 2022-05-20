package com.silenteight.hsbc.bridge.common.configuration;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.common.interceptor.RestParametersInterceptor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class HttpConfigurer implements WebMvcConfigurer {

  private final ApplicationContext context;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new RestParametersInterceptor(context));
  }
}
