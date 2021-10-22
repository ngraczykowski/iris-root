package com.silenteight.warehouse.report.reasoning.download;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reasoning.download.dto.DownloadAiReasoningReportDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.reasoning.AiReasoningReportTestFixtures.*;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.of;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DownloadAiReasoningReportRestController.class,
    DownloadAiReasoningReportConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class DownloadAiReasoningReportRestControllerTest extends BaseRestControllerTest {

  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      fromUriString(
          "/v2/analysis/{analysisId}/reports/AI_REASONING/{id}")
          .build(of("analysisId", ANALYSIS_ID, "id", REPORT_ID))
          .toString();

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/AI_REASONING/{id}")
          .build(of("id", REPORT_ID))
          .toString();

  @MockBean
  DownloadProductionAiReasoningReportUseCase productionUseCase;
  @MockBean
  DownloadSimulationAiReasoningReportUseCase simulationUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadSimulationReport() {
    given(simulationUseCase.activate(REPORT_ID, ANALYSIS_ID))
        .willReturn(createDownloadAiReasoningReportDto(SIMULATION_REPORT_FILENAME));

    String response = get(DOWNLOAD_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", getExpectedContentDisposition(SIMULATION_REPORT_FILENAME))
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(REPORT_CONTENT);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    given(productionUseCase.activate(REPORT_ID))
        .willReturn(createDownloadAiReasoningReportDto(PRODUCTION_REPORT_FILENAME));

    String response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", getExpectedContentDisposition(PRODUCTION_REPORT_FILENAME))
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(REPORT_CONTENT);
  }

  private String getExpectedContentDisposition(String fileName) {
    return format("attachment; filename=\"%s\"", fileName);
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

  private DownloadAiReasoningReportDto createDownloadAiReasoningReportDto(String fileName) {
    InputStream inputStream = toInputStream(REPORT_CONTENT, UTF_8);
    return DownloadAiReasoningReportDto.builder()
        .name(fileName)
        .content(inputStream)
        .build();
  }
}
