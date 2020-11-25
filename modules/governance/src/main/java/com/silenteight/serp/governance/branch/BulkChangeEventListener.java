package com.silenteight.serp.governance.branch;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.branch.ConfigureBranchRequest.ConfigureBranchRequestBuilder;
import com.silenteight.serp.governance.bulkchange.BulkBranchChangeApplied;
import com.silenteight.serp.governance.bulkchange.ReasoningBranchIdToApply;

import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class BulkChangeEventListener {

  private final BranchService branchService;

  @EventListener
  public void bulkChange(BulkBranchChangeApplied event) {
    List<ConfigureBranchRequest> configureBranchRequests =
        event.getReasoningBranchIds()
            .stream()
            .map(branchId -> getConfigureBranchRequest(event, branchId))
            .collect(Collectors.toList());

    branchService.bulkUpdateOrCreateBranches(configureBranchRequests);
  }

  private static ConfigureBranchRequest getConfigureBranchRequest(
      BulkBranchChangeApplied event, ReasoningBranchIdToApply branchId) {

    ConfigureBranchRequestBuilder builder = ConfigureBranchRequest.builder()
        .correlationId(event.getCorrelationId())
        .featureVectorId(branchId.getFeatureVectorId())
        .decisionTreeId(branchId.getDecisionTreeId());

    event.getEnablementChange().ifPresent(builder::enabled);
    event.getSolutionChange().ifPresent(builder::solution);

    return builder.build();
  }
}
