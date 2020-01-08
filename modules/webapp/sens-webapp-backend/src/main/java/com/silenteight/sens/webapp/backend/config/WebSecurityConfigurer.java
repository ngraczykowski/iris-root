package com.silenteight.sens.webapp.backend.config;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.config.token.AdminTokenAuthenticationProvider;
import com.silenteight.sens.webapp.backend.config.token.UserTokenAuthenticationProvider;
import com.silenteight.sens.webapp.backend.security.RestAccessDeniedHandler;
import com.silenteight.sens.webapp.backend.security.dto.PrincipalDtoMapper;
import com.silenteight.sens.webapp.backend.security.login.SaveToDatabaseAuthenticationSuccessListener;
import com.silenteight.sens.webapp.users.user.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.web.filter.CorsFilter;

@KeycloakConfiguration
@RequiredArgsConstructor
public class WebSecurityConfigurer extends KeycloakWebSecurityConfigurerAdapter {

  private final CorsFilter corsFilter;
  private final ObjectMapper objectMapper;
  private final UserService userService;
  private final AdminTokenAuthenticationProvider adminTokenAuthenticationProvider;
  private final UserTokenAuthenticationProvider userTokenAuthenticationProvider;

  // TODO(bgulowaty): move keycloak specific stuff to separate module (WA-83)
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) {
    KeycloakAuthenticationProvider keycloakAuthenticationProvider =
        keycloakAuthenticationProvider();

    // adding proper authority mapper for prefixing role with "ROLE_"
    keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());

    auth
        .authenticationProvider(keycloakAuthenticationProvider)
        .authenticationProvider(adminTokenAuthenticationProvider)
        .authenticationProvider(userTokenAuthenticationProvider);
  }

  @Override
  protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
    return new SessionFixationProtectionStrategy();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);

    http
        // Cross-Site Request Forgery and security Headers disabled for REST API
        .csrf()
        .disable()
        .headers()
        .disable()
        .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .accessDeniedHandler(restAccessDeniedHandler())
        .and()
        .authorizeRequests()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/health").permitAll()
        .antMatchers(RestConstants.MANAGEMENT_PREFIX + "/**").permitAll()
        .anyRequest().authenticated();
  }

  @Bean
  RestAccessDeniedHandler restAccessDeniedHandler() {
    return new RestAccessDeniedHandler(objectMapper);
  }

  @Bean
  PrincipalDtoMapper principalDtoMapper() {
    return new PrincipalDtoMapper();
  }

  @Bean
  ApplicationListener<AbstractAuthenticationEvent> saveToDatabaseAuthenticationSuccessHandler() {
    return new SaveToDatabaseAuthenticationSuccessListener(userService);
  }
}
