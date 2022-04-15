package com.silenteight.serp.governance.common.web.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.RestConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@RequiredArgsConstructor
@Profile("no-rest-api")
class HttpSecurityDenyConfiguration {

  @Autowired
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()
        .anyRequest().denyAll();
  }
}
