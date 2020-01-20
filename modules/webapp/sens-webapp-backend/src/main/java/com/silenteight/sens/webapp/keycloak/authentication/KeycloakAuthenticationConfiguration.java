package com.silenteight.sens.webapp.keycloak.authentication;

import lombok.RequiredArgsConstructor;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;

@KeycloakConfiguration
@RequiredArgsConstructor
class KeycloakAuthenticationConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Bean
  HttpSecurity httpSecurity() throws Exception {
    HttpSecurity httpSecurity = getHttp();
    configure(httpSecurity);

    return httpSecurity;
  }

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new SessionFixationProtectionStrategy();
  }

  @Autowired
  public void configureGlobal(
      AuthenticationManagerBuilder authBuilder,
      KeycloakAuthoritiesExtractor authoritiesExtractor) {

    AuthenticationProvider keycloakAuthProvider =
        new KeycloakUserDetailsAuthenticationProvider(
            keycloakAuthenticationProvider(),
            authoritiesExtractor);

    authBuilder.authenticationProvider(keycloakAuthProvider);
  }

  @Bean
  KeycloakAuthoritiesExtractor keycloakAuthoritiesExtractor() {
    return new KeycloakAuthoritiesExtractor(
        webappRoleNameNormalizer(),
        webappAuthorityNameNormalizer()
    );
  }

  static WebappRoleNameNormalizer webappRoleNameNormalizer() {
    return new WebappRoleNameNormalizer();
  }

  static WebappAuthorityNameNormalizer webappAuthorityNameNormalizer() {
    return new WebappAuthorityNameNormalizer();
  }
}
