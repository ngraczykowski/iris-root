package com.silenteight.warehouse.indexer.query.grouping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@AllArgsConstructor
@Builder
public class FetchGroupedTimeRangedDataRequest {

  @NonNull
  List<String> fields;
  @NonNull
  String dateField;
  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;
  @Default
  List<QueryFilter> queryFilters = new LinkedList<>();

  List<String> getQueryFilterFields() {
    return getQueryFilters()
        .stream()
        .map(QueryFilter::getField)
        .collect(toList());
  }
}
