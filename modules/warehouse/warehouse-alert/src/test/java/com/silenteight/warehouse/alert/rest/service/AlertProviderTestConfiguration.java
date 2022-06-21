package com.silenteight.warehouse.alert.rest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.auth.authorization.RoleAccessorConfiguration;
import com.silenteight.warehouse.alert.rest.RestAlertModule;
import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.indexer.alert.AlertDtoRowMapper;
import com.silenteight.warehouse.indexer.alert.AlertPostgresRepository;
import com.silenteight.warehouse.indexer.alert.AlertRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@ComponentScan(basePackageClasses = {
    RestAlertModule.class,
    DomainModule.class,
    CountryPermissionService.class
})
@Import(RoleAccessorConfiguration.class)
@RequiredArgsConstructor
@Slf4j
public class AlertProviderTestConfiguration {

  @Autowired private JdbcTemplate jdbcTemplate;

  @Bean
  public AlertRepository alertRepository() {
    return new AlertPostgresRepository(jdbcTemplate, new AlertDtoRowMapper());
  }
}
