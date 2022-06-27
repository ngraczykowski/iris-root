/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.warehouse.report.availablereports;

import com.silenteight.warehouse.report.availablereports.ReportTypeListDto.ReportTypeDto;
import com.silenteight.warehouse.report.create.ReportProperties;
import com.silenteight.warehouse.report.create.ReportPropertiesMatcher;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.silenteight.warehouse.common.domain.ReportConstants.PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.SIMULATION;
import static org.assertj.core.api.Assertions.*;

class ReportListServiceTest {

  private final ReportProperties productionReport =
      new ReportProperties("prod-report", PRODUCTION, "", "");
  private final ReportProperties simulationReport =
      new ReportProperties("sim-report", SIMULATION, "", "");

  private final List<ReportProperties> reportProperties =
      List.of(productionReport, simulationReport);

  private final ReportPropertiesMatcher reportPropertiesMatcher =
      new ReportPropertiesMatcher(reportProperties);
  private final ReportListService reportListService =
      new ReportListService(reportPropertiesMatcher);

  @Test
  void productionReportsShouldHaveDateFilter() {
    var reports = reportListService.getReportsList(PRODUCTION);

    assertThat(reports).isEqualTo(List.of(ReportTypeDto.builder()
            .name("analysis/" + PRODUCTION + "/reports/" + productionReport.getName())
            .title(productionReport.getDescription())
            .filter(new FilterDto(FilterType.DATE_RANGE))
            .type(productionReport.getName())
            .download(DownloadType.ASYNC)
        .build()));
  }

  @Test
  void simulationReportsShouldNotHaveDateFilter() {
    var reports = reportListService.getReportsList(SIMULATION);

    assertThat(reports).isEqualTo(List.of(ReportTypeDto.builder()
        .name("analysis/" + SIMULATION + "/reports/" + simulationReport.getName())
        .title(simulationReport.getDescription())
        .filter(new FilterDto(FilterType.NONE))
        .type(simulationReport.getName())
        .download(DownloadType.ASYNC)
        .build()));
  }
}
