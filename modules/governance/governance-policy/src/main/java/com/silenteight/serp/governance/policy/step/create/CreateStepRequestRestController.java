package com.silenteight.serp.governance.policy.step.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.step.create.dto.CreateStepDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CreateStepRequestRestController {

  @NonNull
  private final CreateStepUseCase createStepUseCase;

  @PostMapping(value = "/v1/policies/{id}/steps/")
  @PreAuthorize("isAuthorized('CREATE_STEP')")
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
        .stepType(StepType.BUSINESS_LOGIC)
        .build();
    createStepUseCase.activate(command);
    return ResponseEntity.noContent().build();
  }
}
