package com.silenteight.serp.governance.policy.step.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class PolicyStepsRequestRestController {

  @NonNull
  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  @GetMapping("/v1/policies/{id}/steps")
  @PreAuthorize("isAuthorized('LIST_STEPS')")
  public ResponseEntity<Collection<StepDto>> steps(@PathVariable UUID id) {
    log.info("Listing steps for policy with poliicId={}", id);
    return ResponseEntity.ok(policyStepsRequestQuery.listSteps(id));
  }

  @GetMapping("/v1/policies/{id}/steps/search")
  @PreAuthorize("isAuthorized('LIST_STEPS')")
  public ResponseEntity<Collection<StepDto>> stepsSearch(
      @PathVariable UUID id, @Valid @RequestBody StepSearchCriteriaDto criteria) {

    log.info("Listing steps for policyId={} ans criteria={}", id, criteria);
    return ResponseEntity.ok(policyStepsRequestQuery.listFilteredSteps(id, criteria));
  }
}
