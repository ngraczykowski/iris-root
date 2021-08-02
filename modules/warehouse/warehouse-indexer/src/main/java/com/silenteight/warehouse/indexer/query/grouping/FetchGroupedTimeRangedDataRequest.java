package com.silenteight.warehouse.indexer.query.grouping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class FetchGroupedTimeRangedDataRequest {

  @NonNull
  List<String> fields;
  @NonNull
  List<String> indexes;
  @NonNull
  String dateField;
  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;
  @Default
  boolean onlySolvedAlerts = true;
}
