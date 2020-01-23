package com.silenteight.sens.webapp.backend.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
class ReportConfiguration {

  @NonNull
  private final List<ReportGenerator> reportGenerators;

  @Bean
  ReportProvider reportProvider() {
    RaportProvidersRegistry reportProvider = new RaportProvidersRegistry();
    reportGenerators.forEach(reportProvider::registerReportGenerator);
    return reportProvider;
  }
}
