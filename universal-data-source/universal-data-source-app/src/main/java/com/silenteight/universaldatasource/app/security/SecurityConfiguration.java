package com.silenteight.universaldatasource.app.security;

import lombok.RequiredArgsConstructor;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import net.devh.boot.grpc.server.security.authentication.GrpcAuthenticationReader;
import org.aopalliance.intercept.MethodInterceptor;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, mode = AdviceMode.ASPECTJ)
@EnableConfigurationProperties(SecurityProperties.class)
@RequiredArgsConstructor
class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  private final SecurityProperties properties;

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);

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

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {
    var authMapper = new SimpleAuthorityMapper();
    authMapper.setConvertToUpperCase(true);

    var authProvider = keycloakAuthenticationProvider();
    authProvider.setGrantedAuthoritiesMapper(authMapper);

    auth.authenticationProvider(authProvider);
  }

  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    return new InMemoryUserDetailsManager();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  AccessDecisionManager accessDecisionManager(MethodInterceptor methodSecurityInterceptor) {
    return ((MethodSecurityInterceptor) methodSecurityInterceptor).getAccessDecisionManager();
  }

  @Bean
  GrpcAuthenticationReader grpcAuthenticationReader() {
    return new BasicGrpcAuthenticationReader();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    var encoders = Map.of(
        "bcrypt", new BCryptPasswordEncoder(),
        "pbkdf2", new Pbkdf2PasswordEncoder(),
        "argon2", new Argon2PasswordEncoder()
    );
    return new DelegatingPasswordEncoder("bcrypt", encoders);
  }
}
