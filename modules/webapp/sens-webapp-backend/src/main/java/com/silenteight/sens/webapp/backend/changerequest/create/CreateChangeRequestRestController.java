package com.silenteight.sens.webapp.backend.changerequest.create;

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
class CreateChangeRequestRestController {

  @NonNull
  private final CreateChangeRequestUseCase createChangeRequestUseCase;

  @PostMapping("/change-requests")
  @PreAuthorize("isAuthorized('CREATE_CHANGE_REQUEST')")
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateChangeRequestDto dto,
      @RequestHeader(CORRELATION_ID_HEADER) UUID correlationId,
      Authentication authentication) {

    log.debug(CHANGE_REQUEST, "Requested to create Change Request.");

    RequestCorrelation.set(correlationId);
    CreateChangeRequestCommand command = CreateChangeRequestCommand.builder()
        .bulkChangeId(dto.getBulkChangeId())
        .makerComment(dto.getComment())
        .makerUsername(authentication.getName())
        .createdAt(dto.getCreatedAt())
        .build();
    createChangeRequestUseCase.apply(command);

    log.debug(CHANGE_REQUEST, "Change Request create command accepted");
    return accepted().build();
  }
}
