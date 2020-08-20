package com.silenteight.sens.webapp.backend.decisiontree.details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.Authority.APPROVER_OR_AUDITOR_OR_BUSINESS_OPERATOR;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DecisionTreeDetailsRestController {

  private final DecisionTreeQuery decisionTreeQuery;

  @GetMapping("/decision-trees/{treeId}")
  @PreAuthorize(APPROVER_OR_AUDITOR_OR_BUSINESS_OPERATOR)
  public ResponseEntity<DecisionTreeDto> getDecisionTree(@PathVariable long treeId) {
    return ok(decisionTreeQuery.getDecisionTree(treeId));
  }
}
