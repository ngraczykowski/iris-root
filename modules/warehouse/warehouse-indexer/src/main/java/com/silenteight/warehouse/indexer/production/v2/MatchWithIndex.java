package com.silenteight.warehouse.indexer.production.v2;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v2.Match;

@Value
class MatchWithIndex {

  @NonNull
  Match match;
  @NonNull
  String matchIndexName;
}
