package com.silenteight.sens.webapp.backend.bulkchange.pending;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.bulkchange.BulkChangeDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER_OR_BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class PendingBulkChangeRestController {

  @NonNull
  private final PendingBulkChangeQuery bulkChangeQuery;

  @GetMapping("/bulk-changes/pending")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<List<BulkChangeDto>> listPending() {
    log.debug(CHANGE_REQUEST, "Listing pending Bulk Changes");

    List<BulkChangeDto> bulkChanges = bulkChangeQuery.listPending();

    log.debug(CHANGE_REQUEST, "Found {} pending Bulk Changes", bulkChanges.size());

    return ok(bulkChanges);
  }
}
