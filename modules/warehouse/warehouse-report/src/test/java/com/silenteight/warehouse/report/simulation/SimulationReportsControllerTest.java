package com.silenteight.warehouse.report.simulation;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reporting.ReportInstanceNotFoundException;

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
import static com.silenteight.warehouse.report.simulation.SimulationReportsController.*;
import static com.silenteight.warehouse.report.simulation.SimulationTestConstants.REPORT_DEFINITION_DTO;
import static java.lang.String.format;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    SimulationReportsController.class,
    SimulationReportsControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class SimulationReportsControllerTest extends BaseRestControllerTest {

  private static final long TIMESTAMP = 1622009305142L;
  private static final ReportInstance REPORT_INSTANCE = new ReportInstance(TIMESTAMP);

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

  @MockBean
  private SimulationReportingQuery simulationReportingQuery;

  @MockBean
  private UserAwareReportingService userAwareReportingService;

  @MockBean
  private SimulationService simulationService;

  @Test
  @SneakyThrows
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedGetTenantName() {
    given(simulationReportingQuery.getTenantDtoByAnalysisId(ANALYSIS_ID))
        .willReturn(TENANT_NAME_WRAPPER);

    get(TEST_TENANT_URL).statusCode(OK.value()).body(
        "tenantName", is(ADMIN_TENANT));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetTenantName() {
    get(TEST_TENANT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedGetReportList() {
    given(simulationReportingQuery.getReportsDefinitions(ANALYSIS_ID))
        .willReturn(List.of(REPORT_DEFINITION_DTO));

    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(OK.value())
        .body("[0].id", is(REPORT_DEFINITION_ID))
        .body("[0].title", is(REPORT_NAME));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGetReportList() {
    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenReportGenerated() {
    given(simulationService.createSimulationReport(ANALYSIS_ID, REPORT_DEFINITION_ID))
        .willReturn(REPORT_INSTANCE);

    post(TEST_CREATE_REPORT_URL).statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its401_whenKibanaThrows401() {
    given(simulationService.createSimulationReport(ANALYSIS_ID, REPORT_DEFINITION_ID))
        .willThrow(new OpendistroKibanaClientException(401, "Unauthorized", SAMPLE_KIBANA_URL));

    post(TEST_CREATE_REPORT_URL).statusCode(UNAUTHORIZED.value())
        .body("key", is("KibanaUnauthorizedError"))
        .body("extras.url", is(SAMPLE_KIBANA_URL));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForGeneratingReports() {
    post(TEST_CREATE_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its200_whenInvokedDownloadReport() {
    given(userAwareReportingService.downloadReport(REPORT_DEFINITION_ID, ANALYSIS_ID, TIMESTAMP))
        .willReturn(KIBANA_REPORT_DTO);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    byte[] response = get(TEST_DOWNLOAD_REPORT_URL).statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract().body().asByteArray();

    assertThat(response).isEqualTo(REPORT_CONTENT.getBytes());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { BUSINESS_OPERATOR })
  void its102_whenReportNotReady() {
    given(userAwareReportingService.downloadReport(REPORT_DEFINITION_ID, ANALYSIS_ID, TIMESTAMP))
        .willThrow(ReportInstanceNotFoundException.class);

    get(TEST_DOWNLOAD_REPORT_URL).statusCode(PROCESSING.value());
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { APPROVER, ADMINISTRATOR, ANALYST, AUDITOR, POLICY_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingReport() {
    get(TEST_DOWNLOAD_REPORT_URL).statusCode(FORBIDDEN.value());
  }
}
