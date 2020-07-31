package com.silenteight.sens.webapp.backend.changerequest.pending;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PendingChangeRequestRestController {

  @NonNull
  private final PendingChangeRequestQuery changeRequestQuery;

  @GetMapping(value = "/change-requests", params = "statesFamily=pending")
  public ResponseEntity<List<PendingChangeRequestDto>> pending() {
    log.debug(CHANGE_REQUEST, "Listing pending Change Requests");

    List<PendingChangeRequestDto> changeRequests = changeRequestQuery.listPending();

    log.debug(
        CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());
    return ok(changeRequests);
  }
}
