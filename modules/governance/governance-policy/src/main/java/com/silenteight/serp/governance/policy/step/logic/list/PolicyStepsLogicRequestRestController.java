package com.silenteight.serp.governance.policy.step.logic.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.dto.FeaturesLogicDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class PolicyStepsLogicRequestRestController {

  @NonNull
  private final FeatureLogicRequestQuery policyStepsLogicRequestQuery;

  @GetMapping("/v1/steps/{id}/logic")
  @PreAuthorize("isAuthorized('LIST_STEPS_LOGIC')")
  public ResponseEntity<FeaturesLogicDto> steps(@PathVariable UUID id) {
    log.info("Getting feature logic for stepId={}", id);
    return ResponseEntity.ok(policyStepsLogicRequestQuery.listStepsFeaturesLogic(id));
  }
}
