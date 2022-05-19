package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertItem {

  @NonNull
  String alertName;
  @NonNull
  String messageName;
  @NonNull
  AlertState state;
  @NonNull
  List<String> matchNames;
}
