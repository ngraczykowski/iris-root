package com.silenteight.serp.governance.policy.step.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PolicyStepsRequestRestController {

  @NonNull
  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  @GetMapping("/v1/policies/{id}/steps")
  @PreAuthorize("isAuthorized('LIST_STEPS')")
  public ResponseEntity<Collection<StepDto>> steps(@PathVariable UUID id) {
    return ResponseEntity.ok(policyStepsRequestQuery.listSteps(id));
  }

  @GetMapping("/v1/policies/{id}/steps/search")
  @PreAuthorize("isAuthorized('LIST_STEPS')")
  public ResponseEntity<Collection<StepDto>> stepsSearch(
      @PathVariable UUID id, @Valid @RequestBody StepSearchCriteriaDto criteria) {

    return ResponseEntity.ok(policyStepsRequestQuery.listFilteredSteps(id, criteria));
  }
}
