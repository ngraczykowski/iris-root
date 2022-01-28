package com.silenteight.warehouse.alert.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.token.UserAwareTokenProvider;
import com.silenteight.warehouse.alert.rest.RestAlertModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.common.domain.country.CountryRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
    RestAlertModule.class,
    UserAwareTokenProvider.class,
    DomainModule.class,
    CountryPermissionService.class
})
@RequiredArgsConstructor
@Slf4j
public class AlertProviderTestConfiguration {

  @Bean
  public CountryPermissionService countryPermissionService(CountryRepository countryRepository) {
    return new CountryPermissionService(countryRepository);
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}
