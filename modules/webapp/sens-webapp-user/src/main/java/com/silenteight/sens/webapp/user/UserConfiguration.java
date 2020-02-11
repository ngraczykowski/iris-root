package com.silenteight.sens.webapp.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserConfiguration {

  @Bean
  UserQuery userQuery() {
    return pageable -> null;
  }
}
