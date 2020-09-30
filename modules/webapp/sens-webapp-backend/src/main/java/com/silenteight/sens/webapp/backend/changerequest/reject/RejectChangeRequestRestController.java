package com.silenteight.sens.webapp.backend.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.api.correlation.RequestCorrelation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
class RejectChangeRequestRestController {

  @NonNull
  private final RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @PatchMapping("/change-requests/{id}/reject")
  @PreAuthorize("isAuthorized('REJECT_CHANGE_REQUEST')")
  public ResponseEntity<Void> reject(
      @PathVariable long id,
      @RequestHeader(CORRELATION_ID_HEADER) UUID correlationId,
      @Valid @RequestBody RejectChangeRequestRequestDto request,
      Authentication authentication) {

    log.debug(
        CHANGE_REQUEST, "Requested to reject Change Request. changeRequestId={}, username={}",
        id, authentication.getName());

    RequestCorrelation.set(correlationId);
    RejectChangeRequestCommand command = RejectChangeRequestCommand.builder()
        .changeRequestId(id)
        .rejectorUsername(authentication.getName())
        .rejectorComment(request.getRejectorComment())
        .build();
    rejectChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request rejected. changeRequestId={}", id);
    return accepted().build();
  }
}
