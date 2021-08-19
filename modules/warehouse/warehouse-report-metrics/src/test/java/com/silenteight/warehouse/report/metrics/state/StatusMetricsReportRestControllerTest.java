package com.silenteight.warehouse.report.metrics.state;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.DAY_DEFINITION_ID;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.SIMULATION_DEFINITION_ID;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.GENERATING;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    StatusMetricsReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class StatusMetricsReportRestControllerTest extends BaseRestControllerTest {

  private static final String STATUS_LABEL = "status";
  private static final String REPORT_NAME_LABEL = "reportName";
  private static final String OK_STATUS = "OK";
  private static final String GENERATING_STATUS = "GENERATING";
  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String STATUS_SIMULATION_REPORT_URL =
      fromUriString(
          "/v1/analysis/{analysisId}/definitions/METRICS/{definitionId}/reports/{id}/status")
          .build(of(
              "analysisId", ANALYSIS_ID,
              "definitionId", SIMULATION_DEFINITION_ID,
              "id", REPORT_ID))
          .toString();

  private static final String STATUS_PRODUCTION_REPORT_URL =
      fromUriString(
          "/v1/analysis/production/definitions/METRICS/{definitionId}/reports/{id}/status")
          .build(of("definitionId", DAY_DEFINITION_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  private MetricsReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingSimulationReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(STATUS_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(GENERATING_STATUS))
        .body(
            REPORT_NAME_LABEL,
            is(getReportName(ANALYSIS_ID, SIMULATION_DEFINITION_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedSimulationReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(STATUS_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(OK_STATUS))
        .body(
            REPORT_NAME_LABEL,
            is(getReportName(ANALYSIS_ID, SIMULATION_DEFINITION_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(GENERATING);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(GENERATING_STATUS))
        .body(
            REPORT_NAME_LABEL,
            is(getReportName(PRODUCTION_ANALYSIS_NAME, DAY_DEFINITION_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(REPORT_ID)).thenReturn(DONE);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(OK_STATUS))
        .body(
            REPORT_NAME_LABEL,
            is(getReportName(PRODUCTION_ANALYSIS_NAME, DAY_DEFINITION_ID)));
  }

  private String getReportName(String analysis, String definitionId) {
    return format(
        "analysis/%s/definitions/METRICS/%s/reports/%d", analysis, definitionId, REPORT_ID);
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingSimulationReport() {
    get(STATUS_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
