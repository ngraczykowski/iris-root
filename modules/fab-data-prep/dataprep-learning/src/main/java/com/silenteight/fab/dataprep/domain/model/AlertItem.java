package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AlertItem {

  @NonNull
  String discriminator;
  @NonNull
  String alertName;
  @NonNull
  AlertState state;
  @NonNull
  List<String> matchNames;
}
