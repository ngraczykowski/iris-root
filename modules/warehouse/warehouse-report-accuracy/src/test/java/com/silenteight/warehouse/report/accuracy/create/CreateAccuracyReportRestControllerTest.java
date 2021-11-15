package com.silenteight.warehouse.report.accuracy.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateFromParameterException;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateRangeParametersOrderException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.accuracy.AccuracyReportTestFixtures.*;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    CreateAccuracyReportRestController.class,
    CreateSimulationAccuracyReportUseCase.class,
    CreateProductionAccuracyReportUseCase.class,
    CreateAccuracyReportControllerAdvice.class
})
class CreateAccuracyReportRestControllerTest extends BaseRestControllerTest {

  private static final String CREATE_SIMULATION_REPORT_URL =
      fromUriString("/v2/analysis/{analysisId}/reports/ACCURACY")
          .build(of("analysisId", ANALYSIS_ID))
          .toString();

  private static final String CREATE_PRODUCTION_REPORT_URL =
      fromUriString("/v2/analysis/production/reports/ACCURACY")
          .queryParam("from", OFFSET_DATE_TIME_FROM)
          .queryParam("to", OFFSET_DATE_TIME_TO)
          .build()
          .toString();

  @MockBean
  private CreateSimulationAccuracyReportUseCase simulationUseCase;
  @MockBean
  private CreateProductionAccuracyReportUseCase productionUseCase;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenSimulationReportCreated() {
    given(simulationUseCase.createReport(ANALYSIS_ID)).willReturn(REPORT_INSTANCE);

    post(CREATE_SIMULATION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", getReportLocation(REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its303_whenProductionReportCreated() {
    given(productionUseCase.createReport(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO))
        .willReturn(REPORT_INSTANCE);

    post(CREATE_PRODUCTION_REPORT_URL)
        .statusCode(SEE_OTHER.value())
        .header("Location", getReportLocation(REPORT_ID));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its400_whenInvalidDateRangeParametersOrder() {
    String invalidParametersUrl = fromUriString("/v2/analysis/production/reports/ACCURACY")
        .queryParam("from", QUERY_PARAM_TO)
        .queryParam("to", QUERY_PARAM_FROM)
        .build()
        .toString();

    given(productionUseCase.createReport(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO))
        .willThrow(InvalidDateRangeParametersOrderException.class);

    post(invalidParametersUrl).statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER, APPROVER })
  void its400_whenInvalidDateFromParameter() {
    String invalidParametersUrl = fromUriString("/v2/analysis/production/reports/ACCURACY")
        .queryParam("from", "4021-04-11")
        .queryParam("to", "4021-05-11")
        .build()
        .toString();

    given(productionUseCase.createReport(any(), any()))
        .willThrow(InvalidDateFromParameterException.class);

    post(invalidParametersUrl).statusCode(BAD_REQUEST.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingSimulationReports() {
    post(CREATE_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForCreatingProductionReports() {
    post(CREATE_SIMULATION_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  private static String getReportLocation(long id) {
    return format("ACCURACY/%d/status", id);
  }
}
