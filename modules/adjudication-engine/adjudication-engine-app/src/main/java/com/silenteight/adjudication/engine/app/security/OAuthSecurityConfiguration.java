package com.silenteight.adjudication.engine.app.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, mode = AdviceMode.ASPECTJ)
@EnableConfigurationProperties(OAuthSecurityProperties.class)
@RequiredArgsConstructor
@EnableWebFluxSecurity
@Slf4j
class OAuthSecurityConfiguration {

  private final OAuthSecurityProperties properties;

  @Bean
  public SecurityWebFilterChain configure(ServerHttpSecurity http) {
    if (properties.isDisableSecurity()) {
      log.warn("Disable WebFlux JWT Token security");
      return http
          .authorizeExchange()
          .pathMatchers("/**")
          .permitAll()
          .and()
          .csrf()
          .disable()
          .build();
    }

    log.info("Enabled WebFLux JWT Token security");
    return http.authorizeExchange()
        .pathMatchers("/management/health/**", "/management/prometheus/**", "/status")
        .permitAll()
        .and().csrf().disable().authorizeExchange()
        .anyExchange().authenticated()
        .and().oauth2ResourceServer(OAuth2ResourceServerSpec::jwt).build();
  }
}
