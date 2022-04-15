package com.silenteight.serp.governance.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.changerequest.reject.dto.RejectChangeRequestDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class RejectChangeRequestRestController {

  @NonNull
  private final RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:reject")
  @PreAuthorize("isAuthorized('REJECT_CHANGE_REQUEST')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> reject(
      @PathVariable UUID id,
      @Valid @RequestBody RejectChangeRequestDto request,
      Authentication authentication) {

    log.info("RejectChangeRequest request received, changeRequestId={}", id);

    RejectChangeRequestCommand command = RejectChangeRequestCommand.builder()
        .id(id)
        .rejectorUsername(authentication.getName())
        .rejectorComment(request.getRejectorComment())
        .build();
    rejectChangeRequestUseCase.activate(command);

    log.debug("RejectChangeRequest request processed.");
    return noContent().build();
  }
}
