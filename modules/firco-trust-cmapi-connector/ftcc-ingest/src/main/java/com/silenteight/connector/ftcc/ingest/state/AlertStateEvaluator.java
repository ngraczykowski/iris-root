package com.silenteight.connector.ftcc.ingest.state;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.connector.ftcc.ingest.state.exception.UnknownAlertStateException;
import com.silenteight.connector.ftcc.request.status.MessageCurrentStatusQuery;
import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import java.util.UUID;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.STATE_UNSPECIFIED;

@RequiredArgsConstructor
public class AlertStateEvaluator {

  @NonNull
  private final MessageCurrentStatusQuery messageCurrentStatusQuery;
  @NonNull
  private final AlertStateMapper alertStateMapper;

  public State evaluate(@NonNull UUID batchId, @NonNull UUID messageId) {
    String status = messageCurrentStatusQuery.currentStatus(batchId, messageId);
    State state = alertStateMapper.map(status);

    if (state == STATE_UNSPECIFIED)
      throw new UnknownAlertStateException(status);

    return state;
  }
}
