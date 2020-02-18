package com.silenteight.sens.webapp.backend.user.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.sync.analyst.SyncAnalystService;
import com.silenteight.sens.webapp.user.sync.analyst.dto.SyncAnalystStatsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class SyncAnalystRestController {

  @NonNull
  private final Optional<SyncAnalystService> syncAnalystService;

  @PostMapping("/users/sync/analysts")
  public ResponseEntity<SyncAnalystStatsDto> synchronize() {
    return ok(
        syncAnalystService
            .map(SyncAnalystService::synchronize)
            .orElseThrow(SyncAnalystNotAvailableException::new));
  }

  static class SyncAnalystNotAvailableException extends RuntimeException {

    SyncAnalystNotAvailableException() {
      super("Analyst synchronization not available.");
    }
  }
}
