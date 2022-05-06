package com.silenteight.sep.auth.authentication.oauth2.server.resource;

import lombok.AllArgsConstructor;

import com.silenteight.sep.auth.authentication.WebappAuthorityNameNormalizer;
import com.silenteight.sep.auth.authentication.XssFilter;
import com.silenteight.sep.auth.authorization.AuthorizationProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Profile("!basic-auth && !no-rest-api")
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({ AuthorizationProperties.class })
public class ResourceServerConfiguration extends WebSecurityConfigurerAdapter {

  @Valid
  @NotNull
  private AuthorizationProperties authorizationProperties;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers(header -> header
            .contentTypeOptions().and()
            .xssProtection().block(false).and()
            .httpStrictTransportSecurity().and()
            .frameOptions().sameOrigin())
        .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(resourceServer -> resourceServer
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        .authorizeRequests(request -> request
            .antMatchers(authorizationProperties.getPermitAllUrls()).permitAll()
            .anyRequest().authenticated());
  }

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  XssFilter getXssFilter() {
    return new XssFilter();
  }

  private JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setPrincipalClaimName(authorizationProperties.getPrincipalClaim());
    //TODO: WEB-2240 Remove WebappAuthorityNameNormalizer
    Converter<Jwt, Collection<GrantedAuthority>> authoritiesConverter =
        new KeycloakGrantedAuthoritiesConverter(new WebappAuthorityNameNormalizer());
    jwtConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
    return jwtConverter;
  }
}
