package com.silenteight.warehouse.report.production;

import com.silenteight.warehouse.common.opendistro.kibana.OpendistroKibanaClientException;
import com.silenteight.warehouse.common.testing.rest.BaseRestControllerTest;
import com.silenteight.warehouse.common.web.exception.GenericExceptionControllerAdvice;
import com.silenteight.warehouse.report.reporting.ReportInstanceNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.KIBANA_REPORT_DTO;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_CONTENT;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_DEFINITION_ID;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaReportFixture.REPORT_FILENAME;
import static com.silenteight.warehouse.common.testing.rest.TestRoles.*;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.AI_REASONING_TYPE;
import static com.silenteight.warehouse.report.production.ProductionControllerTestConstants.REPORT_DEFINITION_DTO;
import static com.silenteight.warehouse.report.production.ProductionReportType.AI_REASONING;
import static com.silenteight.warehouse.report.production.ProductionReportsRestController.*;
import static java.lang.String.format;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Import({
    ProductionReportsRestController.class,
    ProductionReportsControllerAdvice.class,
    GenericExceptionControllerAdvice.class
})
class ProductionReportsRestControllerTest extends BaseRestControllerTest {

  private static final ProductionReportType REPORT_TYPE = AI_REASONING;
  private static final long TIMESTAMP = 1622009305142L;
  private static final String DEFINITION_ID = "22c7466f-5084-40fa-beed-add2a78c5a29";
  private static final ReportInstanceReferenceDto REPORT_INSTANCE =
      new ReportInstanceReferenceDto(TIMESTAMP);

  private static final String SAMPLE_KIBANA_URL =
      "/api/reporting/generateReport/KyFUeXkB_K2MGH_UzxPa?timezone=Europe/Warsaw";

  private static final String TEST_REPORT_STATUS_URL =
      "/v1/analysis/production/definitions/" + REPORT_TYPE + "/" + DEFINITION_ID + "/reports/"
          + TIMESTAMP
          + "/status";

  private static final String TEST_LIST_REPORT_DEFINITIONS_URL =
      fromUriString(DEFINITIONS_COLLECTION_URL)
          .build(Map.of(REPORT_TYPE_PARAM, REPORT_TYPE))
          .toString();

  private static final String TEST_CREATE_REPORT_URL = fromUriString(REPORTS_COLLECTION_URL)
      .build(Map.of(
          REPORT_TYPE_PARAM, REPORT_TYPE,
          DEFINITION_ID_PARAM, REPORT_DEFINITION_ID))
      .toString();
  private static final String TEST_DOWNLOAD_REPORT_URL = fromUriString(REPORTS_RESOURCE_URL)
      .build(Map.of(
          REPORT_TYPE_PARAM, REPORT_TYPE,
          DEFINITION_ID_PARAM, REPORT_DEFINITION_ID,
          TIMESTAMP_PARAM, TIMESTAMP))
      .toString();

  private static final ReportStatus TEST_REPORT_STATUS = ReportStatus.buildReportStatusOk(
      "analysis/production/definitions/AI_REASONING/"
          + "22c7466f-5084-40fa-beed-add2a78c5a29/reports/" + TIMESTAMP);

  @MockBean
  ProductionReportingQuery productionReportingQuery;

  @MockBean
  ProductionService productionService;

  @MockBean
  UserAwareReportingService userAwareReportingService;

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedGetKibanaReportDefinitionList() {

    when(productionReportingQuery.getReportsDefinitions(AI_REASONING_TYPE))
        .thenReturn(of(REPORT_DEFINITION_DTO));

    get(TEST_LIST_REPORT_DEFINITIONS_URL)
        .statusCode(OK.value())
        .body("[0].title", is("AI REASONING"));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = {
      USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGetKibanaReportDefinitionList() {
    get(TEST_LIST_REPORT_DEFINITIONS_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenReportGenerated() {
    given(productionService.createProductionReport(REPORT_TYPE, REPORT_DEFINITION_ID))
        .willReturn(REPORT_INSTANCE);

    post(TEST_CREATE_REPORT_URL).statusCode(SEE_OTHER.value())
        .header("Location", "reports/" + TIMESTAMP + REPORT_STATUS);
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its401_whenKibanaThrows401() {
    given(productionService.createProductionReport(REPORT_TYPE, REPORT_DEFINITION_ID))
        .willThrow(new OpendistroKibanaClientException(401, "Unauthorized", SAMPLE_KIBANA_URL));

    post(TEST_CREATE_REPORT_URL).statusCode(UNAUTHORIZED.value())
        .body("key", is("KibanaUnauthorizedError"))
        .body("extras.url", is(SAMPLE_KIBANA_URL));
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = {
      USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForGeneratingReports() {
    post(TEST_CREATE_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_whenInvokedDownloadReport() {
    given(productionService.downloadReport(REPORT_TYPE, REPORT_DEFINITION_ID, TIMESTAMP))
        .willReturn(KIBANA_REPORT_DTO);

    String expectedContentDisposition = format("attachment; filename=\"%s\"", REPORT_FILENAME);
    byte[] response = get(TEST_DOWNLOAD_REPORT_URL).statusCode(OK.value())
        .contentType("text/csv")
        .header("Content-Disposition", expectedContentDisposition)
        .extract().body().asByteArray();

    assertThat(response).isEqualTo(REPORT_CONTENT.getBytes());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its404_whenReportNotFound() {
    given(productionService.downloadReport(REPORT_TYPE, REPORT_DEFINITION_ID, TIMESTAMP))
        .willThrow(
            ReportInstanceNotFoundException.of("admin_tenant", REPORT_DEFINITION_ID, TIMESTAMP));

    get(TEST_DOWNLOAD_REPORT_URL).statusCode(NOT_FOUND.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = {
      USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingReport() {
    get(TEST_DOWNLOAD_REPORT_URL).statusCode(FORBIDDEN.value());
  }

  @Test
  @WithMockUser(username = USERNAME, authorities = { MODEL_TUNER })
  void its200_WhenInvokedGetReportStatus() {
    given(productionService.getReportGeneratingStatus(REPORT_TYPE, DEFINITION_ID, TIMESTAMP))
        .willReturn(TEST_REPORT_STATUS);

    get(TEST_REPORT_STATUS_URL).statusCode(OK.value()).body("status", is("OK"));
  }

  @Test
  @WithMockUser(
      username = USERNAME,
      authorities = { USER_ADMINISTRATOR, AUDITOR, QA, QA_ISSUE_MANAGER })
  void its403_whenNotPermittedRoleForDownloadingReportStatus() {
    get(TEST_REPORT_STATUS_URL).statusCode(FORBIDDEN.value());
  }
}
