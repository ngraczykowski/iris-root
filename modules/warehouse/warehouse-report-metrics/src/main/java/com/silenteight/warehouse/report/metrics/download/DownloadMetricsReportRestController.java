package com.silenteight.warehouse.report.metrics.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.metrics.download.dto.DownloadMetricsReportDto;

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
class DownloadMetricsReportRestController {

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String ID_PARAM = "id";
  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/METRICS/{id}";
  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/METRICS/{id}";

  @NonNull
  private final DownloadProductionMetricsReportUseCase productionUseCase;
  @NonNull
  private final DownloadSimulationMetricsReportUseCase simulationUseCase;

  @GetMapping(DOWNLOAD_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<String> downloadReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(ID_PARAM) long id) {

    DownloadMetricsReportDto reportDto = simulationUseCase.activate(id, analysisId);
    String filename = reportDto.getName();
    String data = reportDto.getContent();
    log.info("Download simulation metrics report request received, analysisId={}, reportId={}",
        analysisId, id);

    return ok()
        .header("Content-Disposition", getContentDisposition(filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<String> downloadReport(@PathVariable(ID_PARAM) long id) {
    DownloadMetricsReportDto reportDto = productionUseCase.activate(id);
    String filename = reportDto.getName();
    String data = reportDto.getContent();
    log.info("Download production metrics report request received, reportId={}", id);
    return ok()
        .header("Content-Disposition", getContentDisposition(filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }

  private static String getContentDisposition(String filename) {
    return format("attachment; filename=\"%s\"", filename);
  }
}
