package com.silenteight.sens.webapp.backend.changerequest.closed;

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
class ClosedChangeRequestRestController {

  @NonNull
  private final ClosedChangeRequestQuery changeRequestQuery;

  @GetMapping(value = "/change-requests", params = "statesFamily=closed")
  public ResponseEntity<List<ClosedChangeRequestDto>> closed() {
    log.debug(CHANGE_REQUEST, "Listing closed Change Requests");

    List<ClosedChangeRequestDto> changeRequests = changeRequestQuery.listClosed();

    log.debug(
        CHANGE_REQUEST, "Found {} closed Change Requests", changeRequests.size());
    return ok(changeRequests);
  }
}
