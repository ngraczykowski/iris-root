package com.silenteight.sens.webapp.backend.report.rest;

import com.silenteight.sens.webapp.backend.report.Report;
import com.silenteight.sens.webapp.backend.report.ReportGenerator;
import com.silenteight.sens.webapp.backend.report.ReportModule;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.common.support.csv.SimpleLinesSupplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.sens.webapp.backend.report.rest.ReportTestFixtures.REPORT_NAME;
import static java.util.Arrays.asList;

@Configuration
@ComponentScan(basePackageClasses = ReportModule.class)
class ReportTestConfiguration {

  @Bean
  DummyReportGenerator dummyReportGenerator() {
    return new DummyReportGenerator();
  }

  private static class DummyReportGenerator implements ReportGenerator {

    @Override
    public String getName() {
      return REPORT_NAME;
    }

    @Override
    public Report generateReport() {
      return new DummyReport();
    }
  }

  private static class DummyReport implements Report {

    @Override
    public String getReportFileName() {
      return REPORT_NAME;
    }

    @Override
    public LinesSupplier getReportContent() {
      return new SimpleLinesSupplier(asList("first_report_line", "second_report_line"));
    }
  }
}
