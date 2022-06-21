package com.silenteight.warehouse.alert.rest.service;

import com.silenteight.sep.auth.authorization.RoleAccessor;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.alert.AlertRepository;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AlertSecurityProperties.class)
public class AlertProviderConfiguration {

  @Bean
  AlertProvider alertProvider(
      CountryPermissionService countryPermissionService,
      RoleAccessor roleAccessor,
      AlertSecurityProperties alertSecurityProperties,
      AlertRepository alertRepository) {
    return new AlertProvider(
        countryPermissionService, roleAccessor, alertSecurityProperties, alertRepository);
  }
}
