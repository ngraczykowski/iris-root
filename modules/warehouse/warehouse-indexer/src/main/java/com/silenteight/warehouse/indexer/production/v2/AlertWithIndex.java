package com.silenteight.warehouse.indexer.production.v2;

import lombok.NonNull;
import lombok.Value;

import com.silenteight.data.api.v2.Alert;

import java.util.List;

@Value
public class AlertWithIndex {

  @NonNull
  Alert alert;
  @NonNull
  String alertIndexName;
  @NonNull
  List<MatchWithIndex> matches;
}
