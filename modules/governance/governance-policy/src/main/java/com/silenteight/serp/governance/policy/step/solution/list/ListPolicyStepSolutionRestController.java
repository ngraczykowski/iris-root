package com.silenteight.serp.governance.policy.step.solution.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ListPolicyStepSolutionRestController {

  @NonNull
  private final PolicyStepSolutionQuery solutionQuery;

  @GetMapping("/v1/policies/steps/solutions")
  public ResponseEntity<List<String>> list() {
    return ok(solutionQuery.list());
  }
}
