package com.silenteight.serp.governance.changerequest.approve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.approve.dto.ApproveChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ApproveChangeRequestRestController {

  @NonNull
  private final ApproveChangeRequestUseCase approveChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:approve")
  @PreAuthorize("isAuthorized('APPROVE_CHANGE_REQUEST')")
  public ResponseEntity<Void> approve(
      @PathVariable UUID id,
      @Valid @RequestBody ApproveChangeRequestDto request,
      Authentication authentication) {

    ApproveChangeRequestCommand command = ApproveChangeRequestCommand.builder()
        .id(id)
        .approverUsername(authentication.getName())
        .approverComment(request.getApproverComment())
        .build();
    approveChangeRequestUseCase.activate(command);
    return noContent().build();
  }
}
