package com.silenteight.warehouse.report.sm.download;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.sm.domain.dto.ReportDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping(ROOT)
class DownloadSimulationMetricsReportRestController {

  private static final String DOWNLOAD_REPORT_URL =
      "/v1/analysis/{analysisId}/definitions/SIMULATION_METRICS/{definitionId}/reports/{id}";
  private static final String ANALYSIS_ID_PARAM = "analysisId";
  private static final String DEFINITION_ID_PARAM = "definitionId";
  private static final String ID_PARAM = "id";

  private final DownloadSimulationMetricsReportUseCase useCase;

  @GetMapping(DOWNLOAD_REPORT_URL)
  @PreAuthorize("isAuthorized('DOWNLOAD_SIMULATION_REPORT')")
  public ResponseEntity<String> downloadReport(
      @PathVariable(ANALYSIS_ID_PARAM) String analysisId,
      @PathVariable(DEFINITION_ID_PARAM) String definitionId,
      @PathVariable(ID_PARAM) long id) {
    ReportDto reportDto = useCase.activate(id);
    String filename = reportDto.getFilename();
    String data = reportDto.getContent();

    return ok()
        .header("Content-Disposition", getContentDisposition(filename))
        .header("Content-Type", "text/csv")
        .body(data);
  }

  private String getContentDisposition(String filename) {
    return format("attachment; filename=\"%s\"", filename);
  }
}
