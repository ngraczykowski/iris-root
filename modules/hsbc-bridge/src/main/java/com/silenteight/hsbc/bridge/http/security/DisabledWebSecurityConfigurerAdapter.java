package com.silenteight.hsbc.bridge.http.security;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

class DisabledWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.httpBasic().disable()
        .authorizeRequests()
        .antMatchers("/**").permitAll()
        .anyRequest().permitAll()
        .and()
        .cors().and()
        .csrf().disable();
  }
}
