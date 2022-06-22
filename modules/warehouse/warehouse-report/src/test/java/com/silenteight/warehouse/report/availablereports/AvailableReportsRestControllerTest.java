/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.warehouse.report.availablereports;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.availablereports.ReportTypeListDto.ReportTypeDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import static java.util.List.of;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;

@Import(AvailableReportsRestController.class)
class AvailableReportsRestControllerTest extends BaseRestControllerTest {

  private static final String TYPE = "production";

  private static final ReportTypeDto REPORT_DTO_1 = ReportTypeDto.builder()
      .name("analysis/production/reports/AI_REASONING")
      .type("AI_REASONING")
      .title("AI Reasoning")
      .filter(new FilterDto(FilterType.DATE_RANGE))
      .build();

  private static final ReportTypeDto REPORT_DTO_2 = ReportTypeDto.builder()
      .name("analysis/production/reports/ACCURACY")
      .type("ACCURACY")
      .title("Accuracy")
      .filter(new FilterDto(FilterType.DATE_RANGE))
      .build();

  @MockBean
  ReportListService reportListService;

  @Test
  void its200() {
    when(reportListService.getReportsList(TYPE)).thenReturn(of(REPORT_DTO_1, REPORT_DTO_2));
    get("/v2/analysis/" + TYPE + "/reports")
        .statusCode(OK.value())
        .body("[0].name", is(REPORT_DTO_1.getName()))
        .body("[0].type", is(REPORT_DTO_1.getType()))
        .body("[0].title", is(REPORT_DTO_1.getTitle()))
        .body("[0].filter.type", is(REPORT_DTO_1.getFilter().getType().name()))
        .body("[0].download", is(DownloadType.ASYNC.name()))
        .body("[1].name", is(REPORT_DTO_2.getName()))
        .body("[1].type", is(REPORT_DTO_2.getType()))
        .body("[1].title", is(REPORT_DTO_2.getTitle()))
        .body("[1].filter.type", is(REPORT_DTO_2.getFilter().getType().name()))
        .body("[1].download", is(DownloadType.ASYNC.name()));
  }
}
