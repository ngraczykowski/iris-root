package com.silenteight.warehouse.report.reasoning.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reasoning.download.dto.DownloadAiReasoningReportDto;

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
class DownloadAiReasoningReportRestController {

  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String ID_PARAM = "id";
  private static final String DOWNLOAD_SIMULATION_REPORT_URL =
      "/v2/analysis/{analysisId}/reports/AI_REASONING/{id}";

  private static final String DOWNLOAD_PRODUCTION_REPORT_URL =
      "/v2/analysis/production/reports/AI_REASONING/{id}";

  @NonNull
  private final DownloadProductionAiReasoningReportUseCase productionUseCase;
  @NonNull
  private final DownloadSimulationAiReasoningReportUseCase simulationUseCase;

  @GetMapping(DOWNLOAD_PRODUCTION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<Resource> downloadProductionReport(@PathVariable(ID_PARAM) long id) {

    log.info("Download production AI Reasoning report on demand request received, reportId={}", id);
    DownloadAiReasoningReportDto reportDto = productionUseCase.activate(id);
    log.debug(
        "Download production AI Reasoning report request processed, reportId={}, reportName={}",
        id, reportDto.getName());

    return ok()
        .header(CONTENT_DISPOSITION, getContentDisposition(reportDto.getName()))
        .contentType(APPLICATION_OCTET_STREAM)
        .body(new InputStreamResource(reportDto.getContent()));
  }

  @GetMapping(DOWNLOAD_SIMULATION_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<Resource> downloadReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(ID_PARAM) long id) {

    log.info(
        "Download simulation AI Reasoning report on demand request received, "
            + "analysisId={}, reportId={}", id, analysisId);

    DownloadAiReasoningReportDto reportDto = simulationUseCase.activate(id, analysisId);
    log.debug(
        "Download simulation AI Reasoning report request processed, reportId={}, reportName={}",
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
