package com.silenteight.sens.webapp.backend.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ApproveChangeRequestRestController {

  @NonNull
  private final ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @PatchMapping("/change-requests/{id}/approve")
  public ResponseEntity<Void> approve(
      @PathVariable long id,
      @RequestHeader(CORRELATION_ID_HEADER) UUID correlationId,
      @Valid @RequestBody ApproveChangeRequestDto request,
      Authentication authentication) {

    log.debug(
        CHANGE_REQUEST, "Requested to approve Change Request. changeRequestId={}, username={}",
        id, authentication.getName());

    RequestCorrelation.set(correlationId);
    ApproveChangeRequestCommand command = ApproveChangeRequestCommand.builder()
        .changeRequestId(id)
        .approverUsername(authentication.getName())
        .approverComment(request.getApproverComment())
        .build();
    approveChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request approved. changeRequestId={}", id);
    return accepted().build();
  }
}
