package com.silenteight.warehouse.report.production;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportStatus;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.SEE_OTHER;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class ProductionReportsRestController {

  static final String DEFINITION_ID_PARAM = "definitionId";
  static final String TIMESTAMP_PARAM = "timestamp";
  static final String REPORT_TYPE_PARAM = "type";
  static final String REPORT_STATUS = "/status";

  static final String DEFINITIONS_COLLECTION_NAME =
      "analysis/production/definitions/{" + REPORT_TYPE_PARAM + "}";
  static final String DEFINITIONS_RESOURCE_NAME =
      DEFINITIONS_COLLECTION_NAME + "/{" + DEFINITION_ID_PARAM + "}";

  static final String DEFINITIONS_COLLECTION_URL =
      "/v1/" + DEFINITIONS_COLLECTION_NAME;
  static final String DEFINITIONS_RESOURCE_URL =
      "/v1/" + DEFINITIONS_RESOURCE_NAME;
  static final String REPORTS_COLLECTION_URL = DEFINITIONS_RESOURCE_URL + "/reports";
  static final String REPORTS_RESOURCE_URL = REPORTS_COLLECTION_URL + "/{" + TIMESTAMP_PARAM + "}";

  static final String REPORTS_RESOURCE_NAME =
      DEFINITIONS_COLLECTION_NAME + "/{" + DEFINITION_ID_PARAM + "}/reports/{"
          + TIMESTAMP_PARAM + "}";

  static final String REPORT_STATUS_URL =
      "/v1/" + REPORTS_RESOURCE_NAME + REPORT_STATUS;

  @NonNull
  private ProductionReportingQuery productionReportingQuery;

  @NonNull
  private ProductionService productionService;

  @GetMapping(DEFINITIONS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('LIST_PRODUCTION_ON_DEMAND_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getProductionReportDefinitionDto(
      @PathVariable(REPORT_TYPE_PARAM) ProductionReportType reportType) {

    log.debug("Getting report definition list for reportType={}", reportType);
    return ok()
        .body(productionReportingQuery.getReportsDefinitions(reportType));
  }

  @PostMapping(REPORTS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(REPORT_TYPE_PARAM) ProductionReportType reportType) {

    log.info("Create production report request received, definitionId={},reportType={}",
        definitionId, reportType);

    ReportInstanceReferenceDto reportInstance =
        productionService.createProductionReport(reportType, definitionId);

    log.debug("Create production report request processed, definitionId={},reportType={}",
        definitionId, reportType);

    return status(SEE_OTHER)
        .header("Location", "reports/" + reportInstance.getGetInstanceReferenceId() + REPORT_STATUS)
        .build();
  }

  @GetMapping(REPORTS_RESOURCE_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<byte[]> downloadReport(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(TIMESTAMP_PARAM) String timestamp,
      @PathVariable(REPORT_TYPE_PARAM) ProductionReportType reportType) {

    log.info(
        "Download production report request received, definitionId={},timestamp={},reportType={}",
        definitionId, timestamp, reportType);

    KibanaReportDto kibanaReportDto =
        productionService.downloadReport(reportType, definitionId, valueOf(timestamp));

    String filename = kibanaReportDto.getFilename();
    String data = kibanaReportDto.getContent();

    log.debug(
        "Download production report request processed,"
            + " definitionId={},timestamp={},reportType={}, reportName={}",
        definitionId, timestamp, reportType, filename);

    return ok()
        .header("Content-Disposition", format("attachment; filename=\"%s\"", filename))
        .header("Content-Type", "text/csv")
        .body(data.getBytes());
  }

  @GetMapping(REPORT_STATUS_URL)
  @PreAuthorize("isAuthorized('CREATE_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<ReportStatus> getReportStatus(
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(TIMESTAMP_PARAM) String timestamp,
      @PathVariable(REPORT_TYPE_PARAM) ProductionReportType reportType) {

    log.debug("Request for report status received, definitionId={},timestamp={},reportType={}",
        definitionId, timestamp, reportType);

    ReportStatus reportStatus =
        productionService.getReportGeneratingStatus(reportType, definitionId, valueOf(timestamp));

    log.debug(
        "Request for report status processed,"
            + " definitionId={},timestamp={},reportType={},reportStatus={}",
        definitionId, timestamp, reportType, reportStatus.getStatus());

    return ResponseEntity.ok(reportStatus);
  }
}
