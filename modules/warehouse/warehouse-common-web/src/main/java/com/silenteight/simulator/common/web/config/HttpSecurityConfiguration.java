package com.silenteight.warehouse.common.web.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.web.rest.RestConstants;
import com.silenteight.warehouse.common.web.security.RestAccessDeniedHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
class HttpSecurityConfiguration {

  private final CorsFilter corsFilter;
  private final RestAccessDeniedHandler restAccessDeniedHandler;

  @Autowired
  public void configure(HttpSecurity http) throws Exception {
    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .accessDeniedHandler(restAccessDeniedHandler)
        .and()
        .authorizeRequests()
        .antMatchers(RestConstants.OPENAPI_PREFIX + "/**").permitAll()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()
        .anyRequest().authenticated();
  }
}
