package com.silenteight.serp.governance.policy.step.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.apache.commons.collections4.list.UnmodifiableList.unmodifiableList;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class SetPolicyStepsOrderRequestRestController {

  @NonNull
  private final SetPolicyStepsUseCase setPolicyStepsUseCase;

  @PutMapping(value = "/v1/policies/{id}/steps-order")
  @PreAuthorize("isAuthorized('EDIT_POLICY')")
  public ResponseEntity<Void> setStepsOrder(
      @PathVariable UUID id,
      @Valid @RequestBody List<UUID> stepsOrder,
      Authentication authentication) {

    setPolicyStepsUseCase.activate(id, unmodifiableList(stepsOrder), authentication.getName());

    return ResponseEntity.ok().build();
  }
}
