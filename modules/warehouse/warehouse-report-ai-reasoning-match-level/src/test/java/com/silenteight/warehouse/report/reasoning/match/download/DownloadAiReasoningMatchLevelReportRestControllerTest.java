package com.silenteight.warehouse.report.reasoning.match.download;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reasoning.match.AiReasoningMatchLevelReportTestFixtures;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DownloadAiReasoningMatchLevelReportRestController.class,
    DownloadAiReasoningMatchLevelReportConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class DownloadAiReasoningMatchLevelReportRestControllerTest extends BaseRestControllerTest {

  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      fromUriString(
          "/v1/analysis/{analysisId}/definitions/AI_REASONING_MATCH_LEVEL/"
              + "{definitionId}/reports/{id}")
          .build(of(
              "analysisId", AiReasoningMatchLevelReportTestFixtures.ANALYSIS_ID,
              "definitionId", AiReasoningMatchLevelReportTestFixtures.SIMULATION_DEFINITION_ID,
              "id", AiReasoningMatchLevelReportTestFixtures.REPORT_ID))
          .toString();

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      fromUriString("/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/"
          + "{definitionId}/reports/{id}")
          .build(
              of("definitionId", AiReasoningMatchLevelReportTestFixtures.MONTH_DEFINITION_ID,
                  "id", AiReasoningMatchLevelReportTestFixtures.REPORT_ID))
          .toString();

  @MockBean
  private DownloadAiReasoningMatchLevelReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadSimulationReport() {
    given(useCase.activate(AiReasoningMatchLevelReportTestFixtures.REPORT_ID)).willReturn(
        createFileDto());

    String expectedContentDisposition = String.format(
        "attachment; filename=%s",
        AiReasoningMatchLevelReportTestFixtures.REPORT_FILENAME);
    String response = get(DOWNLOAD_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(AiReasoningMatchLevelReportTestFixtures.STUB_REPORT_RESPONSE);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    given(useCase.activate(AiReasoningMatchLevelReportTestFixtures.REPORT_ID)).willReturn(
        createFileDto());

    String expectedContentDisposition = String.format(
        "attachment; filename=%s",
        AiReasoningMatchLevelReportTestFixtures.REPORT_FILENAME);
    String response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(AiReasoningMatchLevelReportTestFixtures.STUB_REPORT_RESPONSE);
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

  private FileDto createFileDto() {
    InputStream inputStream = IOUtils.toInputStream(
        AiReasoningMatchLevelReportTestFixtures.STUB_REPORT_RESPONSE, UTF_8);
    return FileDto.builder()
        .name(AiReasoningMatchLevelReportTestFixtures.REPORT_FILENAME)
        .content(inputStream)
        .sizeInBytes(-1L)
        .build();
  }
}
