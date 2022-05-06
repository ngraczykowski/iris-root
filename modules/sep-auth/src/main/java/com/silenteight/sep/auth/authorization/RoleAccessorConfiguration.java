package com.silenteight.sep.auth.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleAccessorConfiguration {

  @Bean
  RoleAccessor roleAccessor() {
    return new RoleAccessor();
  }
}
