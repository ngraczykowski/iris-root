package com.silenteight.connector.ftcc.ingest.state;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.fab.api.v1.AlertMessageStored.State;

import java.util.Map;

import static com.silenteight.proto.fab.api.v1.AlertMessageStored.State.STATE_UNSPECIFIED;

@RequiredArgsConstructor
class AlertStateMapper {

  @NonNull
  private final Map<String, State> mappings;

  State map(@NonNull String status) {
    return mappings.getOrDefault(status, STATE_UNSPECIFIED);
  }
}
