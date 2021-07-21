package com.silenteight.warehouse.report.sm.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static java.util.Map.of;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateSimulationMetricsReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class CreateReportsRestControllerTest extends BaseRestControllerTest {

  private static final long TIMESTAMP = 1622009305142L;
  private static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);

  private static final String CREATE_REPORT_URL =
      fromUriString("/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/{id}/reports")
      .build(of("analysisId", ANALYSIS_ID, "id", "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e"))
      .toString();

  @MockBean
  private CreateSimulationMetricsReportUseCase useCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenReportCreated() {
    given(useCase.activate(ANALYSIS_ID)).willReturn(REPORT_INSTANCE);

    post(CREATE_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + "/status");
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingReports() {
    post(CREATE_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
