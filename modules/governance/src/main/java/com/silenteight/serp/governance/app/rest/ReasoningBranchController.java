package com.silenteight.serp.governance.app.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.serp.common.rest.RestConstants;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;
import com.silenteight.serp.governance.branchquery.dto.ReasoningBranchSummaryDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(RestConstants.ROOT)
@RequiredArgsConstructor
@Slf4j
class ReasoningBranchController {

  private final ReasoningBranchFinder finder;

  @GetMapping("/v1/decision-trees/{decisionTreeId}/reasoning-branches")
  public ResponseEntity<List<ReasoningBranchSummaryDto>> getReasoningBranches(
      @PathVariable long decisionTreeId) {

    List<ReasoningBranchSummaryDto> branches = finder.findAllByDecisionTreeId(decisionTreeId)
        .stream()
        .map(ReasoningBranchSummaryDto::new)
        .collect(toList());

    return ResponseEntity.ok(branches);
  }

  @GetMapping("/v1/decision-trees/{decisionTreeId}/reasoning-branches/{featureVectorId}")
  public ResponseEntity<ReasoningBranchSummaryDto> getReasoningBranches(
      @PathVariable long decisionTreeId, @PathVariable long featureVectorId) {

    ReasoningBranchSummary summary = finder
        .getByDecisionTreeIdAndFeatureVectorId(decisionTreeId, featureVectorId);

    return ResponseEntity.ok(new ReasoningBranchSummaryDto(summary));
  }
}
