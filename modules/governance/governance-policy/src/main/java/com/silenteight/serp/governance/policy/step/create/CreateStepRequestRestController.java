package com.silenteight.serp.governance.policy.step.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.step.create.dto.CreateStepDto;

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
class CreateStepRequestRestController {

  @NonNull
  private final CreateStepUseCase createStepUseCase;

  @PostMapping("/v1/policies/{id}/steps")
  @PreAuthorize("isAuthorized('CREATE_STEP')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> create(
      @PathVariable UUID id,
      @Valid @RequestBody CreateStepDto createStepDto,
      Authentication authentication) {

    CreateStepCommand command = CreateStepCommand
        .builder()
        .policyId(id)
        .stepId(createStepDto.getId())
        .name(createStepDto.getName())
        .description(createStepDto.getDescription())
        .solution(createStepDto.getSolution())
        .createdBy(authentication.getName())
        .stepType(createStepDto.getType())
        .build();
    createStepUseCase.activate(command);
    return ResponseEntity.noContent().build();
  }
}
