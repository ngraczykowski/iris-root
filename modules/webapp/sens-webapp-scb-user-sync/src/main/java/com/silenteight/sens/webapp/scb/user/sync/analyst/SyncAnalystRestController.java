package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.audit.api.AuditLog;
import com.silenteight.sens.webapp.backend.security.Authority;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.SyncAnalystStatsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.silenteight.sens.webapp.audit.api.AuditMarker.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class SyncAnalystRestController {

  @NonNull
  private final Optional<SyncAnalystsUseCase> syncAnalystsUseCase;

  @NonNull
  private final AuditLog auditLog;

  @PostMapping("/users/sync/analysts")
  @PreAuthorize(Authority.ADMIN)
  public ResponseEntity<SyncAnalystStatsDto> synchronize() {
    auditLog.logInfo(USER_MANAGEMENT, "Synchronizing Analysts");

    return ok(
        syncAnalystsUseCase
            .map(SyncAnalystsUseCase::synchronize)
            .orElseThrow(SyncAnalystNotAvailableException::new));
  }

  static class SyncAnalystNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1295617718134359709L;

    SyncAnalystNotAvailableException() {
      super("Analyst synchronization not available.");
    }
  }
}
