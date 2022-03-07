package com.silenteight.serp.governance.policy.step.order.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class SetPolicyStepsOrderRequestRestController {

  @NonNull
  private final SetPolicyStepsUseCase setPolicyStepsUseCase;

  @PutMapping("/v1/policies/{id}/steps-order")
  @PreAuthorize("isAuthorized('EDIT_POLICY')")
  public ResponseEntity<Void> setStepsOrder(
      @PathVariable UUID id,
      @Valid @RequestBody List<UUID> stepsOrder,
      Authentication authentication) {

    log.info("Setting step order for policy. policyId={},stepsOrder={}", id, stepsOrder);

    SetPolicyStepsOrderCommand command = SetPolicyStepsOrderCommand
        .builder()
        .policyId(id)
        .stepsOrder(stepsOrder)
        .updatedBy(authentication.getName())
        .build();
    setPolicyStepsUseCase.activate(command);

    log.debug("Setting step order for policy request processed.");
    return ResponseEntity.ok().build();
  }
}
