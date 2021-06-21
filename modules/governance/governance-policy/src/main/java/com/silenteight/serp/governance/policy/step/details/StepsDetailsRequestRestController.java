package com.silenteight.serp.governance.policy.step.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class StepsDetailsRequestRestController {

  @NonNull
  private final StepRequestQuery stepRequestQuery;

  @GetMapping("/v1/steps/{id}")
  @PreAuthorize("isAuthorized('STEP_DETAILS')")
  public ResponseEntity<StepDto> steps(@PathVariable UUID id) {
    return ResponseEntity.ok(stepRequestQuery.getStep(id));
  }
}
