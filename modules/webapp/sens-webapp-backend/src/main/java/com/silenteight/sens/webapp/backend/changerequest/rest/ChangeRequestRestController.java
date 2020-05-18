package com.silenteight.sens.webapp.backend.changerequest.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.changerequest.approve.ApproveChangeRequestUseCase;
import com.silenteight.sens.webapp.backend.changerequest.domain.ChangeRequestQuery;
import com.silenteight.sens.webapp.backend.changerequest.dto.ChangeRequestDto;
import com.silenteight.sens.webapp.backend.changerequest.rest.dto.ApproveChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.created;
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

  @GetMapping("/change-requests")
  @PreAuthorize(APPROVER)
  public ResponseEntity<List<ChangeRequestDto>> pending() {
    log.debug(CHANGE_REQUEST, "Listing pending Change Requests");

    List<ChangeRequestDto> changeRequests = changeRequestQuery.listPending();

    log.debug(
        CHANGE_REQUEST, "Found {} pending Change Requests", changeRequests.size());
    return ok(changeRequests);
  }

  @PostMapping("/change-requests-approvals")
  @PreAuthorize(APPROVER)
  public ResponseEntity<Void> approve(
      @RequestBody ApproveChangeRequestDto request, Authentication authentication) {
    log.debug(
        CHANGE_REQUEST, "Requested to approve Change Request, request={}, username={}",
        request, authentication.getName());

    final long approvalId =
        approveChangeRequestUseCase.apply(request.toCommand(authentication.getName()));

    log.debug(CHANGE_REQUEST, "Approved Change Request, approvalId={}", approvalId);
    return created(URI.create("/change-requests-approvals/" + approvalId)).build();
  }
}
