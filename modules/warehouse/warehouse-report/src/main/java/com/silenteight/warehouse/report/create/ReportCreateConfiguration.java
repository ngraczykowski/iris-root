package com.silenteight.warehouse.report.create;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.country.CountryPermissionService;
import com.silenteight.warehouse.report.generation.ReportGenerationService;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ReportsDefinition.class)
public class ReportCreateConfiguration {

  @Valid
  private final ReportsDefinition reportsDefinition;

  @Bean
  ReportRequestService reportRequestService(
      ReportPersistenceService reportPersistenceService,
      ReportGenerationService reportProvider,
      CountryPermissionService countryPermissionService
  ) {
    return new ReportRequestService(
        reportPersistenceService,
        reportPropertiesMatcher(),
        reportProvider,
        countryPermissionService);
  }

  @Bean
  ReportPropertiesMatcher reportPropertiesMatcher() {
    return new ReportPropertiesMatcher(reportsDefinition.getReports());
  }
}
