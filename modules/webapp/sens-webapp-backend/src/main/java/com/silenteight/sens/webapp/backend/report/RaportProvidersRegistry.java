package com.silenteight.sens.webapp.backend.report;

import com.silenteight.sens.webapp.backend.report.exception.ReportNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class RaportProvidersRegistry implements ReportProvider {

  private final Map<String, ReportGenerator> generatorsByName = new ConcurrentHashMap<>();

  void registerReportGenerator(ReportGenerator generator) {
    generatorsByName.put(generator.getName(), generator);
  }

  @Override
  public ReportGenerator getReportGenerator(String reportName) {
    return Optional
        .ofNullable(generatorsByName.get(reportName))
        .orElseThrow(
            () -> new ReportNotFoundException("Could not find report with name: " + reportName));
  }
}
