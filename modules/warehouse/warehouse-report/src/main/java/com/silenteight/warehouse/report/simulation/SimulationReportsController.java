package com.silenteight.warehouse.report.simulation;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.KibanaReportDto;
import com.silenteight.warehouse.report.reporting.ReportInstanceNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;
import com.silenteight.warehouse.report.reporting.UserAwareReportingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.Long.valueOf;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.PROCESSING;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
@Slf4j
public class SimulationReportsController {

  static final String ANALYSIS_ID_PARAM = "analysisId";
  static final String DEFINITION_ID_PARAM = "definitionId";
  static final String TIMESTAMP_PARAM = "timestamp";

  static final String ANALYSIS_RESOURCE_URL = "/v1/analysis/{" + ANALYSIS_ID_PARAM + "}";
  static final String TENANT_SUBRESOURCE_URL = ANALYSIS_RESOURCE_URL + "/tenant";
  static final String DEFINITIONS_COLLECTION_URL = ANALYSIS_RESOURCE_URL + "/definitions";
  static final String DEFINITIONS_RESOURCE_URL =
      DEFINITIONS_COLLECTION_URL + "/{" + DEFINITION_ID_PARAM + "}";
  static final String REPORTS_COLLECTION_URL = DEFINITIONS_RESOURCE_URL + "/reports";
  static final String REPORTS_RESOURCE_URL = REPORTS_COLLECTION_URL + "/{" + TIMESTAMP_PARAM + "}";

  @NonNull
  private SimulationReportingQuery simulationReportingQuery;

  @NonNull
  private UserAwareReportingService userAwareReportingService;

  @NonNull
  private SimulationService simulationService;

  @GetMapping(TENANT_SUBRESOURCE_URL)
  @PreAuthorize("isAuthorized('GET_SIMULATION_TENANT')")
  public ResponseEntity<TenantDto> getTenantNameWrapper(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId) {

    return ok().body(simulationReportingQuery.getTenantDtoByAnalysisId(analysisId));
  }

  @GetMapping(DEFINITIONS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('LIST_SIMULATION_REPORTS')")
  public ResponseEntity<List<ReportDefinitionDto>> getReportsDtoList(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId) {

    return ok().body(simulationReportingQuery.getReportsDefinitions(analysisId));
  }

  @PostMapping(REPORTS_COLLECTION_URL)
  @PreAuthorize("isAuthorized('CREATE_SIMULATION_REPORT')")
  public ResponseEntity<Void> createReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(DEFINITION_ID_PARAM) String definitionId) {

    ReportInstance reportInstance =
        simulationService.createSimulationReport(analysisId, definitionId);
    return status(HttpStatus.SEE_OTHER)
        .header("Location", "reports/" + reportInstance.getTimestamp())
        .build();
  }

  @GetMapping(REPORTS_RESOURCE_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<byte[]> downloadReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(TIMESTAMP_PARAM) String timestamp) {

    try {
      KibanaReportDto kibanaReportDto =
          userAwareReportingService.downloadReport(definitionId, analysisId, valueOf(timestamp));

      String filename = kibanaReportDto.getFilename();
      String data = kibanaReportDto.getContent();

      return ok()
          .header("Content-Disposition", format("attachment; filename=\"%s\"", filename))
          .header("Content-Type", "text/csv")
          .body(data.getBytes());
    } catch (ReportInstanceNotFoundException e) {
      return status(PROCESSING).build();
    }
  }
}
