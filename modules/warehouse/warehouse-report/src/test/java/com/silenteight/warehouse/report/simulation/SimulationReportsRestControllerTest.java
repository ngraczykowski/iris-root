package com.silenteight.warehouse.report.simulation;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.indexer.analysis.AnalysisDoesNotExistException;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.*;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.indexer.analysis.NewAnalysisEventFixture.ANALYSIS_ID;
import static com.silenteight.warehouse.report.reporting.ReportServiceTestConstants.TENANT_NAME_WRAPPER;
import static com.silenteight.warehouse.report.simulation.SimulationReportsRestController.*;
import static com.silenteight.warehouse.report.simulation.SimulationTestConstants.REPORT_DEFINITION_DTO;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    SimulationReportsRestController.class,
    SimulationReportsControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class SimulationReportsRestControllerTest extends BaseRestControllerTest {

  private static final long TIMESTAMP = 1622009305142L;
  private static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);

  private static final String SAMPLE_KIBANA_URL =
      "/api/reporting/generateReport/KyFUeXkB_K2MGH_UzxPa?timezone=Europe/Warsaw";

  private static final String TEST_TENANT_URL = fromUriString(TENANT_SUBRESOURCE_URL)
      .build(of(ANALYSIS_ID_PARAM, ANALYSIS_ID))
      .toString();

  private static final String TEST_LIST_REPORT_DEFINITIONS_URL =
      fromUriString(DEFINITIONS_COLLECTION_URL)
          .build(of(ANALYSIS_ID_PARAM, ANALYSIS_ID))
          .toString();

  private static final String TEST_CREATE_REPORT_URL = fromUriString(REPORTS_COLLECTION_URL)
      .build(of(
          ANALYSIS_ID_PARAM, ANALYSIS_ID,
          DEFINITION_ID_PARAM, REPORT_DEFINITION_ID))
      .toString();
  private static final String TEST_DOWNLOAD_REPORT_URL = fromUriString(REPORTS_RESOURCE_URL)
      .build(of(
          ANALYSIS_ID_PARAM, ANALYSIS_ID,
          DEFINITION_ID_PARAM, REPORT_DEFINITION_ID,
          TIMESTAMP_PARAM, TIMESTAMP))
      .toString();

  private static final String TEST_DOWNLOAD_REPORT_STATUS =
      TEST_DOWNLOAD_REPORT_URL + REPORT_STATUS;

  @MockBean
  private SimulationReportingQuery simulationReportingQuery;

  @MockBean
  private SimulationReportsDefinitionsUseCase simulationReportsDefinitionsUseCase;

  @MockBean
  private SimulationService simulationService;

  @Test
  @SneakyThrows
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetTenantName() {
    given(simulationReportingQuery.getTenantDtoByAnalysisId(ANALYSIS_ID))
        .willReturn(TENANT_NAME_WRAPPER);

    get(TEST_TENANT_URL).statusCode(OK.value()).body(
        "tenantName", is(ADMIN_TENANT));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGetTenantName() {
    get(TEST_TENANT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetReportList() {
    given(simulationReportsDefinitionsUseCase.activate(ANALYSIS_ID))
        .willReturn(List.of(REPORT_DEFINITION_DTO));

    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(OK.value())
        .body("[0].id", is(REPORT_DEFINITION_ID))
        .body("[0].title", is(REPORT_NAME));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its404_whenAnalysisNotFound() {
    given(simulationReportsDefinitionsUseCase.activate(ANALYSIS_ID))
        .willThrow(AnalysisDoesNotExistException.class);

    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGetReportList() {
    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its303_whenReportGenerated() {
    given(simulationService.createSimulationReport(ANALYSIS_ID, REPORT_DEFINITION_ID))
        .willReturn(REPORT_INSTANCE);

    post(TEST_CREATE_REPORT_URL).statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + REPORT_STATUS);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its500_whenKibanaThrows401() {
    given(simulationService.createSimulationReport(ANALYSIS_ID, REPORT_DEFINITION_ID))
        .willThrow(new OpendistroKibanaClientException(401, "Unauthorized", SAMPLE_KIBANA_URL));

    post(TEST_CREATE_REPORT_URL).statusCode(INTERNAL_SERVER_ERROR.value())
        .body("key", is("KibanaUpstreamServiceError"))
        .body("extras.url", is(SAMPLE_KIBANA_URL))
        .body("extras.errorCode", is(UNAUTHORIZED.value()));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGeneratingReports() {
    post(TEST_CREATE_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadReport() {
    given(simulationService.downloadReport(ANALYSIS_ID, REPORT_DEFINITION_ID, TIMESTAMP))
        .willReturn(KIBANA_REPORT_DTO);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    byte[] response = get(TEST_DOWNLOAD_REPORT_URL).statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract().body().asByteArray();

    assertThat(response).isEqualTo(REPORT_CONTENT.getBytes());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingReport() {
    get(TEST_DOWNLOAD_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDowloadingReportStatus() {
    get(TEST_DOWNLOAD_REPORT_STATUS).statusCode(FORBIDDEN.value());
  }
}
