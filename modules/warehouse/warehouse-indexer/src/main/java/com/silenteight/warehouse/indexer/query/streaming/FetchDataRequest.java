package com.silenteight.warehouse.indexer.query.streaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class FetchDataRequest {

  @NonNull
  ReportFieldDefinitions fieldsDefinitions;
  @NonNull
  List<String> indexes;
  @NonNull
  String dateField;
  @NonNull
  OffsetDateTime from;
  @NonNull
  OffsetDateTime to;
  @Default
  List<QueryFilter> queryFilters = new LinkedList<>();
  @NonNull
  String name;

  String[] getIndexesArray() {
    return indexes.toArray(String[]::new);
  }

  String[] getFieldsNamesArray() {
    return fieldsDefinitions.getNames().toArray(String[]::new);
  }
}
