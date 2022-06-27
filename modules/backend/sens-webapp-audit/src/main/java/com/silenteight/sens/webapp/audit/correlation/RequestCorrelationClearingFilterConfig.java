package com.silenteight.sens.webapp.audit.correlation;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import javax.servlet.DispatcherType;

@Configuration
class RequestCorrelationClearingFilterConfig {

  @Bean
  FilterRegistrationBean requestCorrelationClearingFilter() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(new RequestCorrelationClearingFilter());
    registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
    return registration;
  }
}
