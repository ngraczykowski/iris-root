package com.silenteight.serp.governance.policy.step.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.step.edit.dto.EditStepDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class EditStepRequestRestController {

  @NonNull
  private final EditStepUseCase editStepUseCase;

  @PatchMapping("/v1/steps/{id}")
  @PreAuthorize("isAuthorized('EDIT_STEP')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> edit(
      @PathVariable UUID id,
      @Valid @RequestBody EditStepDto editStepDto,
      Authentication authentication) {

    EditStepCommand command = EditStepCommand
        .builder()
        .id(id)
        .name(editStepDto.getName())
        .description(editStepDto.getDescription())
        .solution(editStepDto.getSolution())
        .updatedBy(authentication.getName())
        .build();
    editStepUseCase.activate(command);

    return ResponseEntity.noContent().build();
  }

}
