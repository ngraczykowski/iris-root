package com.silenteight.sens.webapp.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.common.support.csv.SimpleLinesSupplier;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.silenteight.sens.webapp.report.ReportTestFixtures.REPORT_NAME;
import static java.lang.String.format;
import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toList;

@Configuration
@ComponentScan(basePackageClasses = { ReportModule.class })
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
    public Report generateReport(Map<String, String> parameters) {
      return new DummyReport(parameters);
    }
  }

  @RequiredArgsConstructor
  private static class DummyReport implements Report {

    private static final String MANDATORY_PARAM = "paramA";

    @NonNull
    private final Map<String, String> parameters;

    @Override
    public String getReportFileName() {
      return REPORT_NAME;
    }

    @Override
    public LinesSupplier getReportContent() {
      if (parameters.get(MANDATORY_PARAM) == null)
        throw new IllegalParameterException(MANDATORY_PARAM + " not provided");

      List<String> lines = new LinkedList<>();
      lines.add("Requested query parameters:");
      lines.addAll(
          parameters.entrySet()
              .stream()
              .sorted(comparingByKey())
              .map(e -> format("%s=%s", e.getKey(), e.getValue()))
              .collect(toList()));

      return new SimpleLinesSupplier(lines);
    }
  }
}
