package com.silenteight.serp.governance.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.changerequest.approve.dto.ApproveChangeRequestDto;

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
class ApproveChangeRequestRestController {

  @NonNull
  private final ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:approve")
  @PreAuthorize("isAuthorized('APPROVE_CHANGE_REQUEST')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> approve(
      @PathVariable UUID id,
      @Valid @RequestBody ApproveChangeRequestDto request,
      Authentication authentication) {
    log.info("Request to approve change request received, changeRequestId={}", id);
    ApproveChangeRequestCommand command = ApproveChangeRequestCommand.builder()
        .id(id)
        .approverUsername(authentication.getName())
        .approverComment(request.getApproverComment())
        .build();
    approveChangeRequestUseCase.activate(command);
    log.debug("Request to approve change request processed, changeRequestId={}.", id);
    return noContent().build();
  }
}
