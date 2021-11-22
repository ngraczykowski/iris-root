package com.silenteight.warehouse.report.metrics.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.name.ReportFileName;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.*;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DownloadMetricsReportRestController.class,
    DownloadMetricsReportConfiguration.class,
    DownloadMetricsReportControllerAdvice.class
})
class DownloadMetricsReportRestControllerTest extends BaseRestControllerTest {

  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      fromUriString(
          "/v2/analysis/{analysisId}/reports/METRICS/{id}")
          .build(of("analysisId", ANALYSIS_ID, "id", REPORT_ID))
          .toString();

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/METRICS/{id}")
          .build(of("id", REPORT_ID))
          .toString();

  @MockBean
  MetricsReportDataQuery query;
  @MockBean
  MetricsReportService reportService;
  @MockBean
  ReportFileName reportFileName;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadSimulationReport() {
    given(query.getReport(REPORT_ID)).willReturn(REPORT_DTO);
    when(reportFileName.getReportName(any())).thenReturn(REPORT_FILENAME);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get(DOWNLOAD_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(REPORT_CONTENT);
    verify(reportService).removeReport(REPORT_ID);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    given(query.getReport(REPORT_ID)).willReturn(REPORT_DTO);
    when(reportFileName.getReportName(any())).thenReturn(REPORT_FILENAME);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(REPORT_CONTENT);
    verify(reportService).removeReport(REPORT_ID);
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingSimulationReport() {
    get(DOWNLOAD_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingProductionReport() {
    get(DOWNLOAD_PRODUCTION_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
