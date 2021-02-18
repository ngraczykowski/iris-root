package com.silenteight.hsbc.bridge.bulk.query;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class GetStatusQuery {
  @NonNull UUID bulkId;
}
