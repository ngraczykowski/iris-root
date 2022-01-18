package com.silenteight.sens.webapp.permission.domain;

import lombok.NonNull;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan
@EnableJpaRepositories
class PermissionDomainConfiguration {

  @Bean
  PermissionQuery permissionQuery(@NonNull PermissionRepository repository) {
    return new PermissionQuery(repository);
  }
}
