package com.silenteight.sens.webapp.backend.external.apps;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(ExternalAppsProperties.class)
@Configuration
class ExternalAppsConfiguration {

  @Bean
  ReportingUrlProvider reportingUrlProvider(ExternalAppsProperties externalAppsProperties) {
    return externalAppsProperties::getReportingUrl;
  }
}
