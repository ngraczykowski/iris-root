package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.rbs.download.dto.DownloadRbsReportDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DownloadRbsReportRestController {

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String ID_PARAM = "id";
  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/RB_SCORER/{id}";

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/RB_SCORER/{id}";

  @NonNull
  private final DownloadProductionRbsReportUseCase productionUseCase;

  @NonNull
  private final DownloadSimulationRbsReportUseCase simulationUseCase;

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<String> downloadProductionReport(@PathVariable(ID_PARAM) long id) {
    log.info("Download production Rb Scorer report request received, reportId={}", id);
    DownloadRbsReportDto reportDto = productionUseCase.activate(id);
    String filename = reportDto.getName();
    String data = reportDto.getContent();
    log.debug("Download production Rb Scorer report request processed, reportId={}, reportName={}",
        id, filename);

    return ok()
        .header("Content-Disposition", getContentDisposition(filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }

  @GetMapping(DOWNLOAD_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<String> downloadSimulationReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(ID_PARAM) long id) {

    log.info("Download RB Scorer simulation report request received, analysisId={}, reportId={}",
        analysisId, id);

    DownloadRbsReportDto reportDto = simulationUseCase.activate(id, analysisId);
    String filename = reportDto.getName();
    String data = reportDto.getContent();

    log.debug("Download simulation RB Scorer report request processed, reportId={}, reportName={}",
        id, filename);

    return ok()
        .header("Content-Disposition", getContentDisposition(filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }

  private static String getContentDisposition(String filename) {
    return format("attachment; filename=\"%s\"", filename);
  }
}
