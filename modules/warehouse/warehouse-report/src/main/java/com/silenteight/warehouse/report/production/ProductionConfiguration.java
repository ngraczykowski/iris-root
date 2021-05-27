package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.environment.EnvironmentProperties;
import com.silenteight.warehouse.report.reporting.ReportingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Valid;

@Configuration
@EnableConfigurationProperties(EnvironmentProperties.class)
class ProductionConfiguration {

  @Autowired
  EnvironmentProperties environmentProperties;

  @Autowired
  ReportingService reportingService;

  @Bean
  ProductionReportingQuery productionReportingQuery(
      @Valid EnvironmentProperties environmentProperties,
      ReportingService reportingService) {
    return new ProductionReportingQuery(environmentProperties, reportingService);
  }
}
