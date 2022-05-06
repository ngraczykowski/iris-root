package com.silenteight.sep.auth.authentication.block;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.AuthorizationProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.validation.Valid;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Profile("no-rest-api")
@Configuration
@EnableConfigurationProperties(AuthorizationProperties.class)
@Slf4j
public class BlockAuthenticationConfiguration extends WebSecurityConfigurerAdapter {

  @Valid
  private final AuthorizationProperties authorizationProperties;

  private final AccessDeniedHandler restAccessDeniedHandler;

  @Autowired
  BlockAuthenticationConfiguration(AuthorizationProperties authorizationProperties,
      @Qualifier("restAccessDeniedHandler") AccessDeniedHandler restAccessDeniedHandler) {

    this.restAccessDeniedHandler = restAccessDeniedHandler;
    this.authorizationProperties = authorizationProperties;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
        .exceptionHandling()
        .accessDeniedHandler(restAccessDeniedHandler)
        .and()
        .authorizeRequests(request -> request
            .antMatchers(authorizationProperties.getPermitAllUrls()).permitAll()
            .anyRequest().denyAll());
  }
}
