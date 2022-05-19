package com.silenteight.fab.dataprep.domain.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CreateAlertItem {

  @NonNull
  String alertName;
  @NonNull
  String messageName;
  @NonNull
  List<String> matchNames;
}
