package com.silenteight.warehouse.report.metrics.download;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.metrics.download.dto.DownloadMetricsReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.ANALYSIS_ID;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_CONTENT;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_ID;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.*;
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
  DownloadSimulationMetricsReportUseCase simulationMetricsReportUseCase;

  @MockBean
  DownloadProductionMetricsReportUseCase downloadProductionMetricsReportUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  @SneakyThrows
  void its200_whenInvokedDownloadSimulationReport() {
    when(simulationMetricsReportUseCase.activate(REPORT_ID, ANALYSIS_ID))
        .thenReturn(
            DownloadMetricsReportDto.builder()
                .content(toInputStream(REPORT_CONTENT))
                .name(REPORT_FILENAME)
                .build());

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    InputStream response = get(DOWNLOAD_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asInputStream();

    assertThat(response).hasSameContentAs(toInputStream(REPORT_CONTENT));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    when(downloadProductionMetricsReportUseCase.activate(REPORT_ID))
        .thenReturn(
            DownloadMetricsReportDto.builder()
                .content(toInputStream(REPORT_CONTENT))
                .name(REPORT_FILENAME)
                .build());

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    InputStream response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asInputStream();

    assertThat(response).hasSameContentAs(toInputStream(REPORT_CONTENT));
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
