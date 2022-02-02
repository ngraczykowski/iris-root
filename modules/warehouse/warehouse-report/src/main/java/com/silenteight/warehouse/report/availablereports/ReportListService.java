package com.silenteight.warehouse.report.availablereports;

import lombok.AllArgsConstructor;

import com.silenteight.warehouse.report.availablereports.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.create.ReportProperties;
import com.silenteight.warehouse.report.create.ReportPropertiesMatcher;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.warehouse.common.domain.ReportConstants.IS_PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.SIMULATION;

@AllArgsConstructor
class ReportListService {

  private final ReportPropertiesMatcher reportPropertiesMatcher;

  public List<ReportTypeDto> getReportsList(String type) {

    List<ReportProperties> properties =
        IS_PRODUCTION.test(type)
        ? reportPropertiesMatcher.getFor(PRODUCTION)
        : reportPropertiesMatcher.getFor(SIMULATION);

    return properties.stream()
        .map(prop -> toReportTypeDto(prop, type))
        .collect(Collectors.toList());
  }

  private ReportTypeDto toReportTypeDto(ReportProperties properties, String type) {
    return ReportTypeDto.builder()
        .type(properties.getName())
        .title(properties.getDescription())
        .name(prepareReportName(type, properties.getName()))
        .build();
  }

  private String prepareReportName(String type, String name) {
    return String.format("analysis/%s/reports/%s", type, name);
  }
}
