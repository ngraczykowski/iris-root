package com.silenteight.serp.governance.changerequest.cancel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.NO_CONTENT_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class CancelChangeRequestRestController {

  @NonNull
  private final CancelChangeRequestUseCase cancelChangeRequestUseCase;

  @PostMapping("/v1/changeRequests/{id}:cancel")
  @PreAuthorize("isAuthorized('CANCEL_CHANGE_REQUEST')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> cancel(
      @PathVariable UUID id, Authentication authentication) {

    log.info("Cancel ChangeRequest request received, changeRequestId={}", id);
    CancelChangeRequestCommand command = CancelChangeRequestCommand.builder()
        .id(id)
        .cancellerUsername(authentication.getName())
        .build();
    cancelChangeRequestUseCase.activate(command);
    log.debug("Cancel ChangeRequest request processed");
    return noContent().build();
  }
}
