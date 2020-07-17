package com.silenteight.sens.auth.authentication;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.auth.authorization.AuthorizationFilter;
import com.silenteight.sens.auth.authorization.AuthorizationProperties;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.adapters.springsecurity.filter.KeycloakAuthenticatedActionsFilter;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

@KeycloakConfiguration
@RequiredArgsConstructor
class KeycloakAuthenticationConfiguration extends KeycloakWebSecurityConfigurerAdapter {

  @Bean
  HttpSecurity httpSecurity(AuthorizationFilter authorizationFilter) throws Exception {
    HttpSecurity httpSecurity = getHttp();
    configure(httpSecurity);
    httpSecurity.addFilterBefore(authorizationFilter, KeycloakAuthenticatedActionsFilter.class);

    return httpSecurity;
  }

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
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

  @Bean
  SensKeycloakSpringBootConfigResolver sensKeycloakSpringBootConfigResolver(
      AdapterConfig adapterConfig) {
    return new SensKeycloakSpringBootConfigResolver(adapterConfig);
  }

  static WebappRoleNameNormalizer webappRoleNameNormalizer() {
    return new WebappRoleNameNormalizer();
  }

  static WebappAuthorityNameNormalizer webappAuthorityNameNormalizer() {
    return new WebappAuthorityNameNormalizer();
  }

  @Bean
  AuthorizationFilter authorizationFilter(AuthorizationProperties authorizationProperties) {
    return new AuthorizationFilter(authorizationProperties.paths());
  }
}
