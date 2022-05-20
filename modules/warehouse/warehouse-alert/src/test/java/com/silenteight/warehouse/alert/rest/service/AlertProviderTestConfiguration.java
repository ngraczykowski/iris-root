package com.silenteight.warehouse.alert.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.RoleAccessorConfiguration;
import com.silenteight.warehouse.alert.rest.RestAlertModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan(basePackageClasses = {
    RestAlertModule.class,
    DomainModule.class,
    CountryPermissionService.class
})
@Import(RoleAccessorConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class AlertProviderTestConfiguration {

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
