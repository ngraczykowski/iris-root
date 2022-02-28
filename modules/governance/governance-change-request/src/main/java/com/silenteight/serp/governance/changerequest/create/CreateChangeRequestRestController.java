package com.silenteight.serp.governance.changerequest.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.changerequest.create.dto.CreateChangeRequestDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.CHANGE_REQUEST_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = CHANGE_REQUEST_ENDPOINT_TAG)
class CreateChangeRequestRestController {

  @NonNull
  private final CreateChangeRequestUseCase createChangeRequestUseCase;

  @PostMapping("/v1/changeRequests")
  @PreAuthorize("isAuthorized('CREATE_CHANGE_REQUEST')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateChangeRequestDto dto, Authentication authentication) {

    CreateChangeRequestCommand command = CreateChangeRequestCommand.builder()
        .id(dto.getId())
        .modelName(dto.getModelName())
        .createdBy(authentication.getName())
        .creatorComment(dto.getComment())
        .build();
    createChangeRequestUseCase.activate(command);
    return accepted().build();
  }
}
