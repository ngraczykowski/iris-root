package com.silenteight.warehouse.report.synchronization;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class SynchronizationRestController {

  static final String REPORT_SYNCHRONIZATION_URL = "/v1/reports:synchronize";

  @NonNull
  private final ReportSynchronizationUseCase reportSynchronizationUseCase;

  @PostMapping(REPORT_SYNCHRONIZATION_URL)
  @PreAuthorize("isAuthorized('SYNCHRONIZE_REPORTS')")
  public ResponseEntity<Void> triggerReportSynchronization() {
    reportSynchronizationUseCase.activate();
    return status(OK).build();
  }
}
