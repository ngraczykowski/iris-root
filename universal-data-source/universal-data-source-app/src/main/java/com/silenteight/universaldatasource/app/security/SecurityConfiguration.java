package com.silenteight.universaldatasource.app.security;

import lombok.RequiredArgsConstructor;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, mode = AdviceMode.ASPECTJ)
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final SecurityProperties properties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    if (properties.isDisableSecurity()) {
      http.authorizeRequests().anyRequest().permitAll();
    } else {
      http.authorizeRequests()
          .requestMatchers(EndpointRequest.to("health"))
          .permitAll()
          .and()
          .authorizeRequests()
          .requestMatchers(EndpointRequest.toAnyEndpoint())
          .hasRole("ENDPOINT_ADMIN")
          .and()
          .authorizeRequests()
          .anyRequest()
          .authenticated();
    }

    http
        .csrf().disable()
        .formLogin().disable();
  }

  @Bean
  AccessDecisionManager accessDecisionManager(MethodInterceptor methodSecurityInterceptor) {
    return ((MethodSecurityInterceptor) methodSecurityInterceptor).getAccessDecisionManager();
  }

  @Bean
  GrpcAuthenticationReader grpcAuthenticationReader() {
    return new BasicGrpcAuthenticationReader();
  }
}
