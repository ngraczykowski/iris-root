package com.silenteight.warehouse.report.rbs.download;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.RbsReportService;
import com.silenteight.warehouse.report.rbs.domain.dto.ReportDto;

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
class DownloadReportRestController {

  @NonNull
  private final ReportDataQuery reportDataQuery;
  @NonNull
  private final RbsReportService reportService;

  @GetMapping("/v1/analysis/production/definitions/RB_SCORER/{definitionId}/reports/{id}")
  @PreAuthorize("isAuthorized('DOWNLOAD_PRODUCTION_ON_DEMAND_REPORT')")
  public ResponseEntity<byte[]> downloadReport(
      @PathVariable("definitionId") String definitionId,
      @PathVariable("id") Long id) {

    ReportDto reportDto = reportDataQuery.getReport(id);
    String filename = reportDto.getFilename();
    String data = reportDto.getContent();

    reportService.removeReport(id);

    return ok()
        .header("Content-Disposition", format("attachment; filename=\"%s\"", filename))
        .header("Content-Type", "text/csv")
        .body(data.getBytes());
  }
}
