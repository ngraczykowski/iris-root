package com.silenteight.warehouse.report.status;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.persistence.ReportDto;
import com.silenteight.warehouse.report.persistence.ReportPersistenceService;
import com.silenteight.warehouse.report.persistence.ReportState;
import com.silenteight.warehouse.report.persistence.ReportStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static java.lang.String.format;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(ROOT)
public class ReportStatusRestController {

  private static final String COMMON_REPORT_URL = "/v2/analysis/{type}/reports/{name}/{id}/status";

  @NonNull
  private final ReportPersistenceService reportPersistenceService;

  @GetMapping(COMMON_REPORT_URL)
  public ResponseEntity<ReportStatus> checkReportStatus(
      @PathVariable String type,
      @PathVariable String name,
      @PathVariable long id
  ) {

    log.debug("Get {} report status, reportId={}", name, id);

    ReportDto dto = reportPersistenceService.getReport(id);
    ReportState state = dto.getState();

    return ResponseEntity.ok(state.getReportStatus(getReportName(type, name, id)));
  }

  private static String getReportName(String type, String name, long id) {
    return format("analysis/%s/reports/%s/%d", type, name, id);
  }
}
