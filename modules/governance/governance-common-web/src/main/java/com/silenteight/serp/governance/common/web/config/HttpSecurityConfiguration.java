package com.silenteight.serp.governance.common.web.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@Profile("!no-rest-api")
class HttpSecurityConfiguration {

  private final CorsFilter corsFilter;

  @Autowired
  public void configure(HttpSecurity http) {
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
