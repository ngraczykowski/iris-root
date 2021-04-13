package com.silenteight.hsbc.bridge.http.security;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.auth.HsbcAuthorizationProviderConfigurer;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
class HsbcWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  private final HsbcSecurityConfigurationProperties properties;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(new HsbcAuthorizationProviderConfigurer().create());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    properties.getEndpoints()
        .forEach(http::antMatcher);

    http
        .authorizeRequests()
        .anyRequest()
        .authenticated()
        .and()
        .anonymous().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .cors().and()
        .csrf().disable()
        .addFilterBefore(hsbcJwtFilter(), BasicAuthenticationFilter.class);
  }

  private HsbcJwtHttpSecurityFilter hsbcJwtFilter() throws Exception {
    var filter = new HsbcJwtHttpSecurityFilter();

    AuthenticationSuccessHandler noopAuthSuccessHandler = (req, res, auth) -> {};

    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(noopAuthSuccessHandler);
    return filter;
  }
}
