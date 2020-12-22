package com.silenteight.serp.governance.bulkchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.Builder;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;

import java.util.List;
import java.util.Set;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static com.silenteight.serp.governance.bulkchange.BulkBranchChangeStateMapper.mapToProto;
import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BulkBranchChangeViewMapper {

  static BulkBranchChangeView mapToProtoBulkBranchChangeView(BulkBranchChange change) {
    Builder builder = BulkBranchChangeView.newBuilder()
        .setId(fromJavaUuid(change.getBulkBranchChangeId()))
        .addAllReasoningBranchIds(mapToReasoningBranchIds(change.getReasoningBranchIds()))
        .setState(mapToProto(change.getState()))
        .setCreatedAt(toTimestamp(change.getCreatedAt()));

    if (isApplied(change))
      builder.setAppliedAt(toTimestamp(change.getCompletedAt()));

    if (isRejected(change))
      builder.setRejectedAt(toTimestamp(change.getCompletedAt()));

    if (hasSolutionChange(change))
      builder.setSolutionChange(mapToSolutionChange(change));

    if (hasEnablementChange(change))
      builder.setEnablementChange(EnablementChange.newBuilder()
          .setEnabled(change.getEnablementChange())
          .build());

    return builder.build();
  }

  private static boolean hasEnablementChange(BulkBranchChange change) {
    return change.getEnablementChange() != null;
  }

  private static boolean hasSolutionChange(BulkBranchChange change) {
    return change.getSolutionChange() != null;
  }

  private static boolean isRejected(BulkBranchChange change) {
    return change.getState() == State.REJECTED && change.getCompletedAt() != null;
  }

  private static boolean isApplied(BulkBranchChange change) {
    return change.getState() == State.APPLIED && change.getCompletedAt() != null;
  }

  private static BranchSolutionChange mapToSolutionChange(BulkBranchChange change) {
    return BranchSolutionChange.newBuilder()
        .setSolution(change.getSolutionChange())
        .build();
  }

  private static List<ReasoningBranchId> mapToReasoningBranchIds(
      Set<ReasoningBranchIdToChange> reasoningBranchIds) {

    return reasoningBranchIds.stream()
        .map(BulkBranchChangeViewMapper::mapToReasoningBranchId)
        .collect(toList());
  }

  private static ReasoningBranchId mapToReasoningBranchId(ReasoningBranchIdToChange rbi) {
    return ReasoningBranchId.newBuilder()
        .setDecisionTreeId(rbi.getDecisionTreeId())
        .setFeatureVectorId(rbi.getFeatureVectorId())
        .build();
  }

  public static class UnsupportedStateException extends IllegalStateException {

    private static final long serialVersionUID = 1271379641808917775L;

    public UnsupportedStateException(String message) {
      super(message);
    }
  }
}
