package com.silenteight.sens.webapp.common.support.hibernate;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class NamingConventionConfiguration {

  @Bean
  SensWebAppImplicitNamingStrategy sensWebAppImplicitNamingStrategy() {
    return new SensWebAppImplicitNamingStrategy();
  }

  @Bean
  SensWebAppPhysicalNamingStrategy sensWebAppPhysicalNamingStrategy() {
    return new SensWebAppPhysicalNamingStrategy();
  }
}

