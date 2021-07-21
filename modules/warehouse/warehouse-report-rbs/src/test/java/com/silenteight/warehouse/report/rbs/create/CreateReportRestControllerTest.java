package com.silenteight.warehouse.report.rbs.create;

import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.ReportDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import static com.silenteight.warehouse.common.testing.rest.TestRoles.MODEL_TUNER;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.SEE_OTHER;

@Import({
    CreateRbsReportRestController.class,
    CreateRbsReportConfiguration.class,
    GenericExceptionControllerAdvice.class,
    CreateRbsReportControllerAdvice.class,
})
class CreateReportRestControllerTest extends BaseRestControllerTest {

  private static final long REPORT_ID = 7;
  private static final String ANALYSIS_ID = "123";
  @MockBean
  RbsReportService reportService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenRequestingReportGeneration() {
    when(reportService.createProductionReportInstance(ReportDefinition.DAY))
        .thenReturn(new ReportInstanceReferenceDto(REPORT_ID));

    post("/v1/analysis/production/definitions/RB_SCORER/"
             + "f21af598-c78a-4e3a-a3ac-04e1f3d11ea4/reports")
        .statusCode(SEE_OTHER.value())
        .header("location", is("reports/" + REPORT_ID + "/status"));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its406_whenReportTypeUnknown() {
    post("/v1/analysis/production/definitions/RB_SCORER/UNKNOWN_TYPE/reports")
        .statusCode(NOT_ACCEPTABLE.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenRequestingReportSimGeneration() {
    when(reportService.createSimulationReportInstance(ANALYSIS_ID, ReportDefinition.SIMULATION))
        .thenReturn(new ReportInstanceReferenceDto(REPORT_ID));

    post("/v1/analysis/" + ANALYSIS_ID + "/definitions/RB_SCORER/"
             + "f21af598-c78a-4e3a-a3ac-04e1f3d11ea4/reports")
        .statusCode(SEE_OTHER.value())
        .header("location", is("reports/" + REPORT_ID + "/status"));
  }
}
