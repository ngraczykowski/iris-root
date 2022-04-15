package com.silenteight.sens.webapp.report;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@AllArgsConstructor
class ReportConfiguration {

  @NonNull
  private final List<ReportGenerator> reportGenerators;

  @Bean
  ReportGeneratorFacade reportGeneratorFacade(
      ReportProvider reportProvider, AuditTracer auditTracer) {

    return new ReportGeneratorFacade(reportProvider, auditTracer);
  }

  @Bean
  ReportProvider reportProvider() {
    ReportProvidersRegistry reportProvider = new ReportProvidersRegistry();
    reportGenerators.forEach(reportProvider::registerReportGenerator);
    return reportProvider;
  }
}
