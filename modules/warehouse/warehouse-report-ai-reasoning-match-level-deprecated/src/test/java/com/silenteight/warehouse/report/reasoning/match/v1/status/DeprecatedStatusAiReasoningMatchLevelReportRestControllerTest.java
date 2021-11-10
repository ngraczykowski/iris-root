package com.silenteight.warehouse.report.reasoning.match.v1.status;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.MONTH_DEFINITION_ID;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.PRODUCTION_ANALYSIS_NAME;
import static com.silenteight.warehouse.report.reasoning.match.v1.DeprecatedAiReasoningMatchLevelReportTestFixtures.REPORT_ID;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.DONE;
import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedReportState.GENERATING;
import static java.util.Map.of;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    DeprecatedStatusAiReasoningMatchLevelReportRestController.class,
    GenericExceptionControllerAdvice.class
})
class DeprecatedStatusAiReasoningMatchLevelReportRestControllerTest extends BaseRestControllerTest {

  private static final String STATUS_LABEL = "status";
  private static final String REPORT_NAME_LABEL = "reportName";
  private static final String OK_STATUS = "OK";
  private static final String GENERATING_STATUS = "GENERATING";

  private static final String STATUS_PRODUCTION_REPORT_URL =
      fromUriString(
          "/v1/analysis/production/definitions/AI_REASONING_MATCH_LEVEL/"
              + "{definitionId}/reports/{id}/status")
          .build(of("definitionId", MONTH_DEFINITION_ID, "id", REPORT_ID))
          .toString();

  @MockBean
  private DeprecatedAiReasoningMatchLevelReportStatusQuery reportStatusQuery;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithGenerating_whenRequestingGeneratingProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(
        REPORT_ID)).thenReturn(GENERATING);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(GENERATING_STATUS))
        .body(REPORT_NAME_LABEL, is(getReportName(PRODUCTION_ANALYSIS_NAME, MONTH_DEFINITION_ID)));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200WithDone_whenRequestingFinishedProductionReport() {
    when(reportStatusQuery.getReportGeneratingState(
        REPORT_ID)).thenReturn(DONE);

    get(STATUS_PRODUCTION_REPORT_URL)
        .statusCode(OK.value())
        .body(STATUS_LABEL, is(OK_STATUS))
        .body(
            REPORT_NAME_LABEL,
            is(getReportName(
                PRODUCTION_ANALYSIS_NAME,
                MONTH_DEFINITION_ID)));
  }

  private String getReportName(String analysis, String definitionId) {
    return String.format(
        "analysis/%s/definitions/AI_REASONING_MATCH_LEVEL/%s/reports/%d", analysis, definitionId,
        REPORT_ID);
  }
}
