package com.silenteight.warehouse.alert.rest.service;

import com.silenteight.sep.auth.authorization.RoleAccessor;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
@EnableConfigurationProperties(AlertSecurityProperties.class)
public class AlertProviderConfiguration {

  @Bean
  AlertProvider alertProvider(CountryPermissionService countryPermissionService,
      RoleAccessor roleAccessor,
      ObjectMapper objectMapper,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      AlertSecurityProperties alertSecurityProperties) {
    return new AlertProvider(countryPermissionService,
        roleAccessor, objectMapper, namedParameterJdbcTemplate, alertSecurityProperties);
  }

}
