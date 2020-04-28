package com.silenteight.sens.webapp.backend.report;

import com.silenteight.sens.webapp.backend.report.exception.ReportNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

class ReportProvidersRegistry implements ReportProvider {

  private final Map<String, ReportGenerator> generatorsByName = new ConcurrentHashMap<>();

  void registerReportGenerator(ReportGenerator generator) {
    generatorsByName.put(generator.getName(), generator);
  }

  @Override
  public ReportGenerator getReportGenerator(String reportName) {
    return ofNullable(generatorsByName.get(reportName))
        .orElseThrow(
            () -> new ReportNotFoundException("Could not find report with name: " + reportName));
  }
}
