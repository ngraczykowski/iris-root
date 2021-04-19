package com.silenteight.serp.governance.policy.step.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.step.edit.dto.EditStepDto;

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
class EditStepRequestRestController {

  @NonNull
  private final EditStepUseCase editStepUseCase;

  @PatchMapping(value = "/v1/steps/{id}")
  @PreAuthorize("isAuthorized('EDIT_STEP')")
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
