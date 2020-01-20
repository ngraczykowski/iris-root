package com.silenteight.sens.webapp.backend.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.security.RestAccessDeniedHandler;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
    prePostEnabled = true
)
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
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/health").permitAll()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()
        .anyRequest().authenticated();
  }
}
