package com.silenteight.warehouse.report.synchronization;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class SynchronizationRestController {

  static final String REPORT_SYNCHRONIZATION_URL = "/v1/analysis/production:synchronizePeriodic";

  @NonNull
  private final ReportSynchronizationUseCase reportSynchronizationUseCase;

  @PostMapping(REPORT_SYNCHRONIZATION_URL)
  @PreAuthorize("isAuthorized('SYNCHRONIZE_REPORTS')")
  public ResponseEntity<Void> triggerReportSynchronization() {
    log.debug("Report synchronization request received");
    reportSynchronizationUseCase.activate();
    log.debug("Report synchronization request processed");
    return status(OK).build();
  }
}
