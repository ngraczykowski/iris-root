package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.governance.CreateBulkBranchChangeCommand;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.protocol.utils.Uuids.toJavaUuid;

class BulkBranchChangeMapper {

  BulkBranchChange map(CreateBulkBranchChangeCommand command) {
    var id = toJavaUuid(command.getId());
    var correlationId = toJavaUuid(command.getCorrelationId());
    var reasoningBranchIds = mapBranchIds(command.getReasoningBranchIdsList());

    var change = new BulkBranchChange(id, reasoningBranchIds);
    change.setCorrelationId(correlationId);

    if (command.hasEnablementChange())
      change.setEnablementChange(command.getEnablementChange().getEnabled());

    if (command.hasSolutionChange())
      change.setSolutionChange(command.getSolutionChange().getSolution());

    return change;
  }

  @Nonnull
  private static Set<ReasoningBranchIdToChange> mapBranchIds(
      Collection<ReasoningBranchId> reasoningBranchIds) {

    return reasoningBranchIds
        .stream()
        .map(id -> new ReasoningBranchIdToChange(id.getDecisionTreeId(), id.getFeatureVectorId()))
        .collect(Collectors.toSet());
  }
}
