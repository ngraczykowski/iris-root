package com.silenteight.payments.bridge.firco.security;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(SecurityContextProperties.class)
@RequiredArgsConstructor
class SecurityContextConfiguration {

  @Valid
  private final SecurityContextProperties securityContextProperties;

  @Bean
  SecurityContextUseCase securityContextUseCase() {
    return new SecurityContextUseCase(createAuthorities());
  }

  private Collection<? extends GrantedAuthority> createAuthorities() {
    String[] roles = securityContextProperties.getRoles().split(",");
    return Arrays.stream(roles)
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
