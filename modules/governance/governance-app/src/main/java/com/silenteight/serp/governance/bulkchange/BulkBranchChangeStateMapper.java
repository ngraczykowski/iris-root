package com.silenteight.serp.governance.bulkchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.api.BulkBranchChangeView;
import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;
import com.silenteight.serp.governance.bulkchange.BulkBranchChangeViewMapper.UnsupportedStateException;

import java.util.Map;
import java.util.Map.Entry;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BulkBranchChangeStateMapper {

  private static final Map<BulkBranchChangeView.State, State> STATES_MAPPING = Map.of(
      BulkBranchChangeView.State.STATE_APPLIED, State.APPLIED,
      BulkBranchChangeView.State.STATE_CREATED, State.CREATED,
      BulkBranchChangeView.State.STATE_REJECTED, State.REJECTED
  );

  public static State mapToJava(BulkBranchChangeView.State state) {
    if (!STATES_MAPPING.containsKey(state))
      throw new UnsupportedStateException(
          "Branch bulk change request state is not supported: " + state);

    return STATES_MAPPING.get(state);
  }

  public static BulkBranchChangeView.State mapToProto(State state) {
    return STATES_MAPPING.entrySet().stream()
        .filter(entry -> entry.getValue() == state)
        .map(Entry::getKey)
        .findFirst()
        .orElseThrow(() -> new UnsupportedStateException(
            "Branch bulk change request state is not supported: " + state));
  }
}
