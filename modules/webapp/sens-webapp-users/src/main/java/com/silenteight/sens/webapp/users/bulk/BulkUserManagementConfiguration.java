package com.silenteight.sens.webapp.users.bulk;

import com.silenteight.sens.webapp.users.user.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BulkUserManagementConfiguration {

  @Bean
  BulkUserManagementService bulkUserManagementService(UserService userService) {
    return new BulkUserManagementService(userService);
  }
}
