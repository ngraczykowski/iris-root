package com.silenteight.warehouse.report.sm.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.sm.domain.SimulationMetricsReportService;
import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_CONTENT;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_FILENAME;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DownloadSimulationMetricsReportRestController.class,
    DownloadSimulationMetricsReportConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class DownloadReportsRestControllerTest extends BaseRestControllerTest {


  private static final long REPORT_ID = 4;
  private static final ReportDto REPORT_DTO = ReportDto.of(REPORT_FILENAME, REPORT_CONTENT);
  private static final String DOWNLOAD_REPORT_URL =
      fromUriString("/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/reports/{id}")
          .build(Map.of("analysisId", ANALYSIS_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  SimulationMetricsReportDataQuery query;
  @MockBean
  SimulationMetricsReportService reportService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadReport() {
    given(query.getReport(REPORT_ID)).willReturn(REPORT_DTO);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    String response = get(DOWNLOAD_REPORT_URL)
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
  void its403_whenNotPermittedRoleForDownloadingReport() {
    get(DOWNLOAD_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
