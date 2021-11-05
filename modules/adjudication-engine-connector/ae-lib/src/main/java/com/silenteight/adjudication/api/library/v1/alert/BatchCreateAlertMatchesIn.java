package com.silenteight.adjudication.api.library.v1.alert;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.Collection;

@Builder
@Value
public class BatchCreateAlertMatchesIn {

  @NonNull String alert;
  @NonNull Collection<String> matchIds;
}
