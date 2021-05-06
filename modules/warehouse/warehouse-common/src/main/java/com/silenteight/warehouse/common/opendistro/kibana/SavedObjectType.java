package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SavedObjectType {
  KIBANA_INDEX_PATTERN("index-pattern"),
  SEARCH("search");

  private final String id;
}
