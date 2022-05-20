package com.silenteight.warehouse.indexer.query.sql;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
@RequiredArgsConstructor
public class SingleValueCondition {

  @NonNull
  String field;
  @NonNull
  OffsetDateTime value;
}
