package com.silenteight.sens.webapp.backend.user;

import com.silenteight.sens.webapp.backend.reportscb.UserListRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
class UserConfiguration {

  @Bean
  UserQuery userQuery() {
    return pageable -> null;
  }

  @Bean
  UserListRepository userQueryRepository() {
    return Collections::emptyList;
  }
}
