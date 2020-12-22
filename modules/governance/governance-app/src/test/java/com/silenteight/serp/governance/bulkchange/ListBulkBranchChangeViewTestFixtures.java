package com.silenteight.serp.governance.bulkchange;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.BulkBranchChangeView.EnablementChange;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest;
import com.silenteight.proto.serp.v1.api.ListBulkBranchChangesRequest.StateFilter;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.protocol.utils.Uuids;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;

public class ListBulkBranchChangeViewTestFixtures {

  private static final UUID EXAMPLE_UUID1 = UUID.randomUUID();
  private static final UUID EXAMPLE_UUID2 = UUID.randomUUID();
  private static final OffsetDateTime NOW =
      OffsetDateTime.of(2020, 5, 22, 13, 22, 1, 150, ZoneOffset.UTC);
  public static final BulkBranchChange JAVA_CHANGE_ENABLEMENT = javaChangeEnablement();
  public static final BulkBranchChange JAVA_CHANGE_SOLUTION = javaChangeSolution();
  static final BulkBranchChangeView PROTO_CHANGE_ENABLEMENT = protoChangeEnablement();
  static final BulkBranchChangeView PROTO_CHANGE_SOLUTION = protoChangeSolution();

  static List<State> javaStates() {
    return List.of(State.CREATED, State.APPLIED, State.REJECTED);
  }

  static List<BulkBranchChangeView.State> protoStates() {
    return List.of(
        BulkBranchChangeView.State.STATE_CREATED,
        BulkBranchChangeView.State.STATE_APPLIED,
        BulkBranchChangeView.State.STATE_REJECTED);
  }

  public static ListBulkBranchChangesRequest request() {
    return ListBulkBranchChangesRequest.newBuilder()
        .setStateFilter(StateFilter
            .newBuilder()
            .addAllStates(List.of(BulkBranchChangeView.State.STATE_APPLIED))
            .build())
        .build();
  }

  public static ListBulkBranchChangesRequest requestNoStates() {
    return ListBulkBranchChangesRequest.newBuilder()
        .setStateFilter(StateFilter
            .newBuilder()
            .addAllStates(List.of())
            .build())
        .build();
  }

  private static BulkBranchChangeView protoChangeEnablement() {
    return BulkBranchChangeView.newBuilder()
        .setId(Uuids.fromJavaUuid(EXAMPLE_UUID1))
        .addAllReasoningBranchIds(List.of(
            ReasoningBranchId.newBuilder().setFeatureVectorId(1).setDecisionTreeId(1).build()
        ))
        .setEnablementChange(EnablementChange.newBuilder().setEnabled(true).build())
        .setState(BulkBranchChangeView.State.STATE_REJECTED)
        .setCreatedAt(toTimestamp(JAVA_CHANGE_ENABLEMENT.getCreatedAt()))
        .setRejectedAt(toTimestamp(NOW))
        .build();
  }

  private static BulkBranchChangeView protoChangeSolution() {
    return BulkBranchChangeView.newBuilder()
        .setId(Uuids.fromJavaUuid(EXAMPLE_UUID2))
        .addAllReasoningBranchIds(List.of(
            ReasoningBranchId.newBuilder().setFeatureVectorId(1).setDecisionTreeId(1).build()
        ))
        .setSolutionChange(BranchSolutionChange.newBuilder()
            .setSolution(BranchSolution.BRANCH_FALSE_POSITIVE)
            .build())
        .setState(BulkBranchChangeView.State.STATE_APPLIED)
        .setCreatedAt(toTimestamp(JAVA_CHANGE_SOLUTION.getCreatedAt()))
        .setAppliedAt(toTimestamp(NOW))
        .build();
  }

  private static BulkBranchChange javaChangeEnablement() {
    Set<ReasoningBranchIdToChange> reasoningBranchIds = Set.of(
        new ReasoningBranchIdToChange(1, 1)
    );
    BulkBranchChange bulkBranchChange = new BulkBranchChange(EXAMPLE_UUID1, reasoningBranchIds);

    bulkBranchChange.setCompletedAt(NOW);
    bulkBranchChange.setEnablementChange(true);
    bulkBranchChange.setId(1L);
    bulkBranchChange.setState(State.REJECTED);
    return bulkBranchChange;
  }

  private static BulkBranchChange javaChangeSolution() {
    Set<ReasoningBranchIdToChange> reasoningBranchIds = Set.of(
        new ReasoningBranchIdToChange(1, 1)
    );
    BulkBranchChange bulkBranchChange = new BulkBranchChange(EXAMPLE_UUID2, reasoningBranchIds);

    bulkBranchChange.setCompletedAt(NOW);
    bulkBranchChange.setSolutionChange(BranchSolution.BRANCH_FALSE_POSITIVE);
    bulkBranchChange.setId(2L);
    bulkBranchChange.setState(State.APPLIED);
    return bulkBranchChange;
  }
}
