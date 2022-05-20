package com.silenteight.warehouse.report.create;

import lombok.AllArgsConstructor;

import com.silenteight.warehouse.common.domain.ReportConstants;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ReportPropertiesMatcher {

  private final List<ReportProperties> reportProperties;

  public ReportProperties getFor(String name, String type)
      throws ReportNotAvailableException {
    String reportType = ReportConstants.PRODUCTION.equals(type) ?
                        ReportConstants.PRODUCTION : ReportConstants.SIMULATION;

    return reportProperties.stream()
        .filter(report -> reportType.equalsIgnoreCase(report.getType()))
        .filter(report -> report.getName().equals(name))
        .findFirst()
        .orElseThrow(() -> new ReportNotAvailableException(
            String.format("Report name: %s not found for type: %s", name, reportType)));
  }

  public List<ReportProperties> getFor(String type) {
    return reportProperties.stream()
        .filter(report -> report.getType().equalsIgnoreCase(type))
        .collect(Collectors.toList());
  }

}
