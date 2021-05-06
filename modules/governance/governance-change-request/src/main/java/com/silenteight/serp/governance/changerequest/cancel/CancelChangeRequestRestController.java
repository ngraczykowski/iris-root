package com.silenteight.serp.governance.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.noContent;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CancelChangeRequestRestController {

  @NonNull
  private final CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:cancel")
  @PreAuthorize("isAuthorized('CANCEL_CHANGE_REQUEST')")
  public ResponseEntity<Void> cancel(
      @PathVariable UUID id, Authentication authentication) {

    CancelChangeRequestCommand command = CancelChangeRequestCommand.builder()
        .id(id)
        .cancellerUsername(authentication.getName())
        .build();
    cancelChangeRequestUseCase.activate(command);
    return noContent().build();
  }
}
