package com.silenteight.serp.governance.common.web.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.RestConstants;
import com.silenteight.serp.governance.common.web.security.RestAccessDeniedHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
@Profile("no-rest-api")
class HttpSecurityDenyConfiguration {

  private final RestAccessDeniedHandler restAccessDeniedHandler;

  @Autowired
  public void configure(HttpSecurity http) throws Exception {
    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()
        .exceptionHandling()
        .accessDeniedHandler(restAccessDeniedHandler)
        .and()
        .authorizeRequests()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()
        .anyRequest().denyAll();
  }
}
