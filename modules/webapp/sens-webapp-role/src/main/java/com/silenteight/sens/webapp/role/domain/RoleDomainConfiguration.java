package com.silenteight.sens.webapp.role.domain;

import lombok.NonNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class RoleDomainConfiguration {

  @Bean
  RoleService roleService(@NonNull RoleRepository repository) {
    return new RoleService(repository);
  }

  @Bean
  RoleQuery roleQuery(@NonNull RoleRepository repository) {
    return new RoleQuery(repository);
  }
}
