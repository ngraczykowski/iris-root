package com.silenteight.warehouse.report.accuracy.v1.download;

import com.silenteight.sep.filestorage.api.dto.FileDto;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.InputStream;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.accuracy.v1.DeprecatedAccuracyReportTestFixtures.*;
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
    DeprecatedDownloadAccuracyReportRestController.class,
    DeprecatedDownloadAccuracyReportConfiguration.class,
    GenericExceptionControllerAdvice.class
})
class DeprecatedDownloadAccuracyReportRestControllerTest extends BaseRestControllerTest {

  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      fromUriString(
          "/v1/analysis/{analysisId}/definitions/ACCURACY/{definitionId}/reports/{id}")
          .build(of(
              "analysisId", DeprecatedAccuracyReportTestFixtures.ANALYSIS_ID,
              "definitionId", SIMULATION_DEFINITION_ID,
              "id", REPORT_ID))
          .toString();

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      fromUriString("/v1/analysis/production/definitions/ACCURACY/"
          + "{definitionId}/reports/{id}")
          .build(of("definitionId", DAY_DEFINITION_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  DeprecatedDownloadAccuracyReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadSimulationReport() {
    given(useCase.activate(REPORT_ID)).willReturn(createFileDto());

    String expectedContentDisposition = format("attachment; filename=%s", REPORT_FILENAME);
    String response = get(DOWNLOAD_SIMULATION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(STUB_REPORT_RESPONSE);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadProductionReport() {
    given(useCase.activate(REPORT_ID)).willReturn(createFileDto());

    String expectedContentDisposition = format("attachment; filename=%s", REPORT_FILENAME);
    String response = get(DOWNLOAD_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .contentType("application/octet-stream")
        .header("Content-Disposition", expectedContentDisposition)
        .extract()
        .body()
        .asString();

    assertThat(response).isEqualTo(STUB_REPORT_RESPONSE);
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
    InputStream inputStream = toInputStream(STUB_REPORT_RESPONSE, UTF_8);
    return FileDto.builder()
        .name(REPORT_FILENAME)
        .content(inputStream)
        .sizeInBytes(-1L)
        .build();
  }
}
