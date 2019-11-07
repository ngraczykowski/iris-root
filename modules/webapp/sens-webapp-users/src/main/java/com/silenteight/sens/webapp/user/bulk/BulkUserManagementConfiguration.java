package com.silenteight.sens.webapp.user.bulk;

import com.silenteight.sens.webapp.user.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class BulkUserManagementConfiguration {

  @Bean
  BulkUserManagementService bulkUserManagementService(UserService userService) {
    return new BulkUserManagementService(userService);
  }
}
