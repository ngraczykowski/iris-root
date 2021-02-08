package com.silenteight.serp.governance.policy.step.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PolicyStepsOrderRequestRestController {

  @NonNull
  private final PolicyStepsOrderRequestQuery policyStepsOrderRequestQuery;

  @GetMapping(value = "/v1/policies/{id}/steps-order")
  @PreAuthorize("isAuthorized('LIST_STEPS_ORDER')")
  public ResponseEntity<List<UUID>> steps(@PathVariable UUID id) {
    return ResponseEntity.ok(policyStepsOrderRequestQuery.listStepsOrder(id));
  }
}
