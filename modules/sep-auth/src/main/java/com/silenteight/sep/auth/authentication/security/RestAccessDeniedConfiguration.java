package com.silenteight.sep.auth.authentication.security;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class RestAccessDeniedConfiguration {

  private final ObjectMapper objectMapper;

  @Bean
  RestAccessDeniedHandler restAccessDeniedHandler() {
    return new RestAccessDeniedHandler(objectMapper);
  }
}
