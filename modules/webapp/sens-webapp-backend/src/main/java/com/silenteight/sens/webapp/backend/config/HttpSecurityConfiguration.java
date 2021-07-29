package com.silenteight.sens.webapp.backend.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Order(1)
@RequiredArgsConstructor
class HttpSecurityConfiguration {

  private final CorsFilter corsFilter;

  @Autowired
  public void configure(HttpSecurity http) {
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
