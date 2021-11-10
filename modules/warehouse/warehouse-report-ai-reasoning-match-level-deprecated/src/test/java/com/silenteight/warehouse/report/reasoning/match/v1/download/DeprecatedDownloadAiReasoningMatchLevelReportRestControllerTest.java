package com.silenteight.warehouse.report.reasoning.match.v1.download;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.MONTH_DEFINITION_ID;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.REPORT_FILENAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.STUB_REPORT_RESPONSE;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DeprecatedDownloadAiReasoningMatchLevelReportRestController.class,
    DeprecatedDownloadAiReasoningMatchLevelReportConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class DeprecatedDownloadAiReasoningMatchLevelReportRestControllerTest
    extends BaseRestControllerTest {

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      fromUriString("/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/"
          + "{definitionId}/reports/{id}")
          .build(of("definitionId", MONTH_DEFINITION_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  private DeprecatedDownloadAiReasoningMatchLevelReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    given(useCase.activate(REPORT_ID)).willReturn(createFileDto());

    String expectedContentDisposition = String.format("attachment; filename=%s", REPORT_FILENAME);

    String response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(
        STUB_REPORT_RESPONSE);
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
        STUB_REPORT_RESPONSE, UTF_8);
    return FileDto.builder()
        .name(REPORT_FILENAME)
        .content(inputStream)
        .sizeInBytes(-1L)
        .build();
  }
}
