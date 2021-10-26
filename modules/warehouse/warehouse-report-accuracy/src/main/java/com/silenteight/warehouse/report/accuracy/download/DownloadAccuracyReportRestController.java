package com.silenteight.warehouse.report.accuracy.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.accuracy.download.dto.DownloadAccuracyReportDto;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DownloadAccuracyReportRestController {

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String ID_PARAM = "id";
  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/ACCURACY/{id}";

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/ACCURACY/{id}";

  @NonNull
  private final DownloadProductionAccuracyReportUseCase productionUseCase;
  @NonNull
  private final DownloadSimulationAccuracyReportUseCase simulationUseCase;

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Resource> downloadProductionReport(@PathVariable(ID_PARAM) long id) {

    log.info("Download production Accuracy report on demand request received, reportId={}", id);
    DownloadAccuracyReportDto reportDto = productionUseCase.activate(id);
    log.debug("Download production Accuracy report request processed, reportId={}, reportName={}",
        id, reportDto.getName());

    return ok()
        .header(CONTENT_DISPOSITION, getContentDisposition(reportDto.getName()))
        .contentType(APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(reportDto.getContent()));
  }

  @GetMapping(DOWNLOAD_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<Resource> downloadSimulationReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(ID_PARAM) long id) {

    log.info("Download simulation Accuracy report on demand request received, analysisId={}, "
        + "reportId={}", id, analysisId);

    DownloadAccuracyReportDto reportDto = simulationUseCase.activate(id, analysisId);
    log.debug("Download simulation Accuracy report request processed, reportId={}, reportName={}",
        id, reportDto.getName());

    return ok()
        .header(CONTENT_DISPOSITION, getContentDisposition(reportDto.getName()))
        .contentType(APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(reportDto.getContent()));
  }

  private static String getContentDisposition(String filename) {
    return format("attachment; filename=\"%s\"", filename);
  }
}
