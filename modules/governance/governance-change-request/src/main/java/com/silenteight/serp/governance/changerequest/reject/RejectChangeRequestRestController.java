package com.silenteight.serp.governance.changerequest.reject;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.reject.dto.RejectChangeRequestDto;

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
class RejectChangeRequestRestController {

  @NonNull
  private final RejectChangeRequestUseCase rejectChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:reject")
  @PreAuthorize("isAuthorized('REJECT_CHANGE_REQUEST')")
  public ResponseEntity<Void> reject(
      @PathVariable UUID id,
      @Valid @RequestBody RejectChangeRequestDto request,
      Authentication authentication) {

    RejectChangeRequestCommand command = RejectChangeRequestCommand.builder()
        .id(id)
        .rejectorUsername(authentication.getName())
        .rejectorComment(request.getRejectorComment())
        .build();
    rejectChangeRequestUseCase.activate(command);
    return noContent().build();
  }
}
