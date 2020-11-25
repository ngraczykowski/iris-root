package com.silenteight.serp.governance.bulkchange;

import com.silenteight.serp.governance.bulkchange.BulkBranchChange.State;

import java.util.UUID;

class ChangeAlreadyCompletedException extends RuntimeException {

  private static final long serialVersionUID = 7443782298216996353L;

  ChangeAlreadyCompletedException(UUID changeId, State actualState, State requestedState) {
    super(String.format(
        "Cannot complete Bulk Branch Change, because it has already been completed, "
            + "changeId=%s, currentState=%s, requestedState=%s",
        changeId, actualState, requestedState));
  }
}
