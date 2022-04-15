package com.silenteight.serp.governance.policy.step.order.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class PolicyStepsOrderRequestRestController {

  @NonNull
  private final PolicyStepsOrderRequestQuery policyStepsOrderRequestQuery;

  @GetMapping("/v1/policies/{id}/steps-order")
  @PreAuthorize("isAuthorized('LIST_STEPS_ORDER')")
  public ResponseEntity<List<UUID>> steps(@PathVariable UUID id) {
    log.info("Listing steps for policyId={}", id);
    return ResponseEntity.ok(policyStepsOrderRequestQuery.listStepsOrder(id));
  }
}
