package com.silenteight.serp.governance.solutiondiscrepancy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.protobuf.Uuid;
import com.silenteight.proto.serp.v1.circuitbreaker.SolutionDiscrepancyDetectedEvent;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchesDisabledEvent.Cause;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.branch.BranchService;
import com.silenteight.serp.governance.branch.ConfigureBranchRequest;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class SolutionDiscrepancyHandler {

  private final BranchService branchService;
  private final boolean dryRunEnabled;
  private final TimeSource timeSource;

  ReasoningBranchesDisabledEvent disableReasoningBranches(SolutionDiscrepancyDetectedEvent event) {

    if (dryRunEnabled) {
      log.info(
          "Solution discrepancy detected! Dry-run only, Reasoning Branches will NOT be disabled");
    } else {
      log.info("Solution discrepancy detected! Disabling Reasoning Branches...");
      var correlationId = event.getCorrelationId();
      var branchRequests = event
          .getReasoningBranchesList()
          .stream()
          .map(r -> makeConfigurationRequest(r, correlationId))
          .collect(toList());

      branchService.bulkUpdateBranches(branchRequests);
    }

    return ReasoningBranchesDisabledEvent.newBuilder()
        .addAllReasoningBranches(event.getReasoningBranchesList())
        .setCause(makeCause(event.getDecision().getAlertId()))
        .setCreatedAt(toTimestamp(timeSource.now()))
        .setCorrelationId(event.getCorrelationId())
        .build();
  }

  private static ConfigureBranchRequest makeConfigurationRequest(
      ReasoningBranchId reasoningBranchId, Uuid correlationId) {

    return ConfigureBranchRequest.builder()
        .correlationId(Uuids.toJavaUuid(correlationId))
        .decisionTreeId(reasoningBranchId.getDecisionTreeId())
        .featureVectorId(reasoningBranchId.getFeatureVectorId())
        .enabled(Boolean.FALSE)
        .build();
  }

  private static Cause makeCause(ObjectId alertId) {
    String sourceId = alertId.getSourceId();

    return Cause.newBuilder()
        .setDescription("Alert " + sourceId)
        .setAlertId(alertId)
        .build();
  }
}
