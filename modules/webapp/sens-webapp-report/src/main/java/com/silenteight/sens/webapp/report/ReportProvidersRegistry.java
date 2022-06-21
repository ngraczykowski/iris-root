package com.silenteight.sens.webapp.report;

import com.silenteight.sens.webapp.report.exception.ReportNotFoundException;
import com.silenteight.sens.webapp.report.list.FilterDto;
import com.silenteight.sens.webapp.report.list.ListReportsQuery;
import com.silenteight.sens.webapp.report.list.ReportDto;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

class ReportProvidersRegistry implements ReportProvider, ListReportsQuery {

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

  @Override
  public Collection<ReportDto> listAll() {
    return generatorsByName.values()
        .stream()
        .filter(reportGenerator -> reportGenerator instanceof ConfigurableReport)
        .map(ConfigurableReport.class::cast)
        .filter(ConfigurableReport::isEnabled)
        .map(ReportProvidersRegistry::toReportDto)
        .collect(toList());
  }

  private static ReportDto toReportDto(ConfigurableReport reportGenerator) {
    return ReportDto.builder()
        .name(reportGenerator.getName())
        .type(reportGenerator.getName())
        .label(reportGenerator.getLabel())
        .filter(FilterDto.builder().type(reportGenerator.getFilterType()).build())
        .build();
  }
}
