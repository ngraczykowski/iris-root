package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance.ReportDefinition;
import com.silenteight.warehouse.common.opendistro.elastic.ListReportsInstancesResponse.ReportInstance.ReportDefinitionDetails;

import java.util.List;
import java.util.Set;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.report.reporting.ReportsController.BASE_ANALYSIS_URL_PREFIX;
import static com.silenteight.warehouse.report.reporting.ReportsController.REPORTS;
import static com.silenteight.warehouse.report.reporting.ReportsController.TENANT;

class ReportServiceTestConstants {

  static final String FILE_NAME = "all-alerts-report";
  static final String REPORT_ID = "jn9qVnkBtbh61OJJm2PI";
  static final String REPORT_STATUS = "Success";
  static final Long HITS = 1L;
  static final String ANALYSIS_NAME = "8e9010ae-c3e2-417d--b3ea9bb5a454";

  static final String GET_REPORTS_BY_ANALYSIS_NAME =
      BASE_ANALYSIS_URL_PREFIX + ANALYSIS_NAME + REPORTS;
  static final String GET_TENANT_BY_ANALYSIS_NAME =
      BASE_ANALYSIS_URL_PREFIX + ANALYSIS_NAME + TENANT;

  static final ReportDefinition REPORT_DEFINITION = ReportDefinition.builder()
      .name(FILE_NAME)
      .build();

  static final ReportDefinitionDetails REPORT_DEFINITION_DETAILS = ReportDefinitionDetails.builder()
      .reportDefinition(REPORT_DEFINITION)
      .build();

  static final ReportInstance REPORT_INSTANCE = ReportInstance.builder()
      .id(REPORT_ID)
      .status(REPORT_STATUS)
      .reportDefinitionDetails(REPORT_DEFINITION_DETAILS)
      .build();

  static final ListReportsInstancesResponse LIST_REPORTS_INSTANCES_RESPONSE =
      ListReportsInstancesResponse.builder()
          .totalHits(HITS)
          .reportInstancesList(List.of(REPORT_INSTANCE))
          .build();

  static final Set<KibanaReportDetailsDto> TEST_REPORTDTOS = Set.of(
      KibanaReportDetailsDto.builder()
          .title(FILE_NAME)
          .id(REPORT_ID)
          .build());

  static final KibanaReportsList KIBANA_REPORTS_WRAPPER_TEST = KibanaReportsList.builder()
      .reports(TEST_REPORTDTOS).build();

  static final TenantNameWrapper TENANT_NAME_WRAPPER = TenantNameWrapper.builder()
      .tenantName(ADMIN_TENANT)
      .build();
}
