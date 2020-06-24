package com.silenteight.sens.webapp.backend.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.Authority.BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.CHANGE_REQUEST;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CancelChangeRequestRestController {

  @NonNull
  private final CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @PatchMapping("/change-request/{id}/cancel")
  @PreAuthorize(BUSINESS_OPERATOR)
  public ResponseEntity<Void> cancel(
      @PathVariable long id,
      @RequestHeader(CORRELATION_ID_HEADER) UUID correlationId,
      @Valid @RequestBody CancelChangeRequestDto request,
      Authentication authentication) {

    log.debug(
        CHANGE_REQUEST, "Requested to cancel Change Request. changeRequestId={}, username={}",
        id, authentication.getName());

    RequestCorrelation.set(correlationId);
    CancelChangeRequestCommand command = CancelChangeRequestCommand.builder()
        .changeRequestId(id)
        .cancellerUsername(authentication.getName())
        .cancellerComment(request.getCancellerComment())
        .build();
    cancelChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request cancelled. changeRequestId={}", id);
    return accepted().build();
  }
}
