package com.silenteight.serp.governance.policy.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.edit.dto.EditPolicyDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.PolicyState.ARCHIVED;
import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class EditPolicyRequestRestController {

  @NonNull
  private final EditPolicyUseCase editPolicyUseCase;
  @NonNull
  private final SavePolicyUseCase savePolicyUseCase;
  @NonNull
  private final UsePolicyUseCase usePolicyUseCase;
  @NonNull
  private final ArchivePolicyUseCase archivePolicyUseCase;

  @PatchMapping("/v1/policies/{id}")
  @PreAuthorize("isAuthorized('EDIT_POLICY')")
  public ResponseEntity<Void> edit(
      @PathVariable UUID id,
      @Valid @RequestBody EditPolicyDto editPolicyDto,
      Authentication authentication) {

    String userName = authentication.getName();
    EditPolicyCommand command = EditPolicyCommand
        .builder()
        .id(id)
        .policyName(editPolicyDto.getPolicyName())
        .description(editPolicyDto.getDescription())
        .updatedBy(userName)
        .build();
    editPolicyUseCase.activate(command);
    changePolicyState(id, editPolicyDto.getState(), authentication.getName());

    return ResponseEntity.ok().build();
  }

  private void changePolicyState(UUID id, PolicyState state, String userName) {
    if (SAVED == state)
      savePolicyUseCase.activate(id, userName);
    if (ARCHIVED == state)
      archivePolicyUseCase.activate(id, userName);
    // TODO(kdzieciol): Remove this. (WEB-1092)
    if (IN_USE == state)
      usePolicyUseCase.activate(id, userName);
  }
}
