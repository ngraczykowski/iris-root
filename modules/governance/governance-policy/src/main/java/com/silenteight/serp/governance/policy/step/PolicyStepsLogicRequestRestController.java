package com.silenteight.serp.governance.policy.step;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.dto.FeaturesLogicDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class PolicyStepsLogicRequestRestController {

  @NonNull
  private final FeatureLogicRequestQuery policyStepsLogicRequestQuery;

  @GetMapping(value = "/v1/steps/{id}/logic")
  @PreAuthorize("isAuthorized('LIST_STEPS_LOGIC')")
  public ResponseEntity<FeaturesLogicDto> steps(@PathVariable UUID id) {
    return ResponseEntity.ok(policyStepsLogicRequestQuery.listStepsFeaturesLogic(id));
  }
}
