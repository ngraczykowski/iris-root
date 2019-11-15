package com.silenteight.sens.webapp.user;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.silenteight.sens.webapp.adapter.user")
@EntityScan("com.silenteight.sens.webapp.domain.user")
public class UsersModuleConfiguration {
}
