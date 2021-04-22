package com.silenteight.serp.governance.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.cancel.dto.CancelChangeRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CancelChangeRequestRestController {

  @NonNull
  private final CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @PatchMapping("/changeRequests/{id}:cancel")
  @PreAuthorize("isAuthorized('CANCEL_CHANGE_REQUEST')")
  public ResponseEntity<Void> cancel(
      @PathVariable UUID id,
      @Valid @RequestBody CancelChangeRequestDto request,
      Authentication authentication) {

    CancelChangeRequestCommand command = CancelChangeRequestCommand.builder()
        .id(id)
        .cancellerUsername(authentication.getName())
        .cancellerComment(request.getCancellerComment())
        .build();
    cancelChangeRequestUseCase.activate(command);
    return accepted().build();
  }
}
