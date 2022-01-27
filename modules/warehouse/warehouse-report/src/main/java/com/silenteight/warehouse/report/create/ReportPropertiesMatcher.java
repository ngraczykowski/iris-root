package com.silenteight.warehouse.report.create;

import lombok.AllArgsConstructor;

import com.silenteight.warehouse.common.domain.ReportConstants;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
class ReportPropertiesMatcher {

  private final List<ReportProperties> reportProperties;

  ReportProperties getFor(String name, String type) {
    Map<String, ReportProperties> reports = reportProperties.stream()
        .filter(report -> report.getName().equalsIgnoreCase(name))
        .collect(Collectors.toMap(ReportProperties::getType, Function.identity()));

    return ReportConstants.PRODUCTION.equals(type)
           ? reports.get(ReportConstants.PRODUCTION)
           : reports.get(ReportConstants.SIMULATION);
  }

}
