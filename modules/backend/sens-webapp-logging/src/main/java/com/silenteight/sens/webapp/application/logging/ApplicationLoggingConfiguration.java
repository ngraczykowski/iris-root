/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.sens.webapp.application.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class ApplicationLoggingConfiguration implements WebMvcConfigurer {

  @Bean
  AuthenticationSuccessLogger authenticationSuccessLogger() {
    return new AuthenticationSuccessLogger();
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new SensMdcClearingInterceptor());
  }
}
