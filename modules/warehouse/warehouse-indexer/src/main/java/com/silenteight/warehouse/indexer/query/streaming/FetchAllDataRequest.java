package com.silenteight.warehouse.indexer.query.streaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import java.util.LinkedList;
import java.util.List;

@Value
@AllArgsConstructor
@Builder
public class FetchAllDataRequest {

  @NonNull
  List<String> fields;
  @NonNull
  List<String> indexes;
  @Default
  List<QueryFilter> queryFilters = new LinkedList<>();

  String[] getFieldsNamesArray() {
    return fields.toArray(String[]::new);
  }

  String[] getIndexesArray() {
    return indexes.toArray(String[]::new);
  }
}
