package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestUseCase;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestQuery;
import com.silenteight.sens.webapp.backend.changerequest.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestCommand;
import com.silenteight.sens.webapp.backend.changerequest.reject.RejectChangeRequestUseCase;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER;
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

  @NonNull
  private final ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @NonNull
  private final RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @GetMapping("/change-requests")
  @PreAuthorize(APPROVER)
  public ResponseEntity<List<ChangeRequestDto>> pending() {
    log.debug(CHANGE_REQUEST, "Listing pending Change Requests");

    List<ChangeRequestDto> changeRequests = changeRequestQuery.listPending();

    log.debug(
        CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());
    return ok(changeRequests);
  }

  @PatchMapping("/change-request/{id}/approve")
  @PreAuthorize(APPROVER)
  public ResponseEntity<Void> approve(@PathVariable long id, Authentication authentication) {
    log.debug(
        CHANGE_REQUEST, "Requested to approve Change Request. changeRequestId={}, username={}",
        id, authentication.getName());

    ApproveChangeRequestCommand command = ApproveChangeRequestCommand.builder()
        .changeRequestId(id)
        .approverUsername(authentication.getName())
        .build();
    approveChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request approved. changeRequestId={}", id);
    return ok().build();
  }

  @PatchMapping("/change-request/{id}/reject")
  @PreAuthorize(APPROVER)
  public ResponseEntity<Void> reject(@PathVariable long id, Authentication authentication) {
    log.debug(
        CHANGE_REQUEST, "Requested to reject Change Request. changeRequestId={}, username={}",
        id, authentication.getName());

    RejectChangeRequestCommand command = RejectChangeRequestCommand.builder()
        .changeRequestId(id)
        .rejectorUsername(authentication.getName())
        .build();
    rejectChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request rejected. changeRequestId={}", id);
    return ok().build();
  }
}
