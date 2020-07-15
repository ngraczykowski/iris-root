package com.silenteight.sens.webapp.scb.user.sync.analyst;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.common.rest.Authority;
import com.silenteight.sens.webapp.scb.user.sync.analyst.dto.SyncAnalystStatsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class SyncAnalystRestController {

  @Nullable
  private final SyncAnalystsUseCase syncAnalystsUseCase;

  @PostMapping("/users/sync/analysts")
  @PreAuthorize(Authority.ADMIN_OR_ADMINISTRATOR)
  public ResponseEntity<SyncAnalystStatsDto> synchronize() {
    log.info(USER_MANAGEMENT, "Synchronizing Analysts");
    if (syncAnalystsUseCase == null)
      throw new SyncAnalystNotAvailableException();

    return ok(syncAnalystsUseCase.synchronize());
  }

  static class SyncAnalystNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = 1295617718134359709L;

    SyncAnalystNotAvailableException() {
      super("Analyst synchronization not available.");
    }
  }
}
