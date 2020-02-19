package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchRestController {

  @NonNull
  private final ReasoningBranchDetailsQuery reasoningBranchDetailsQuery;

  @GetMapping("/decision-trees/{treeId}/branches/{branchId}")
  public ResponseEntity<BranchDetailsDto> details(
      @PathVariable long treeId, @PathVariable long branchId) {
    log.debug("Requesting Reasoning Branch details. treeId={}, branchId={}", treeId, branchId);

    return reasoningBranchDetailsQuery.findByTreeIdAndBranchId(treeId, branchId)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
