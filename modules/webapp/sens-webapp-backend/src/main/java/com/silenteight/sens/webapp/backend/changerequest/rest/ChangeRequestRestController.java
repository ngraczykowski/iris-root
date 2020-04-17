package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.sens.webapp.backend.security.Authority.APPROVER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ChangeRequestRestController {

  @NonNull
  private final ChangeRequestQuery changeRequestQuery;

  @GetMapping("/change-requests")
  @PreAuthorize(APPROVER)
  public ResponseEntity<List<ChangeRequestDto>> pending() {
    log.info(CHANGE_REQUEST, "Listing pending Change Requests");

    List<ChangeRequestDto> changeRequests = changeRequestQuery.pending();

    log.info(CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());
    return ok(changeRequests);
  }
}
