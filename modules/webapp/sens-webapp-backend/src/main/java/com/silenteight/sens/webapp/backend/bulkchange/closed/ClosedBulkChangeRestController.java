package com.silenteight.sens.webapp.backend.bulkchange.closed;

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
class ClosedBulkChangeRestController {

  @NonNull
  private final ClosedBulkChangeQuery bulkChangeQuery;

  @GetMapping(value = "/bulk-changes", params = "statesFamily=closed")
  @PreAuthorize(APPROVER_OR_BUSINESS_OPERATOR)
  public ResponseEntity<List<BulkChangeDto>> listClosed() {
    log.debug(CHANGE_REQUEST, "Listing closed Bulk Changes");

    List<BulkChangeDto> bulkChanges = bulkChangeQuery.listClosed();

    log.debug(CHANGE_REQUEST, "Found {} closed Bulk Changes", bulkChanges.size());

    return ok(bulkChanges);
  }
}
