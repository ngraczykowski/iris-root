package com.silenteight.sens.webapp.backend.report.config;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.report.api.ReportGenerator;
import com.silenteight.sens.webapp.backend.report.api.ReportProvider;

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
    ReportProvidersRegistry reportProvider = new ReportProvidersRegistry();
    reportGenerators.forEach(reportProvider::registerReportGenerator);
    return reportProvider;
  }
}
