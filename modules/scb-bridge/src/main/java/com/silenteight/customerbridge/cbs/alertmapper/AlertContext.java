package com.silenteight.customerbridge.cbs.alertmapper;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import javax.annotation.Nullable;

import static java.util.Collections.emptyList;

@Value
@Builder
public class AlertContext {

  @Nullable
  String lastDecBatchId;
  @Nullable
  String typeOfRec;
  boolean lastDecisionPresent;
  @Nullable
  String partyName;
  @NonNull
  @Default
  List<String> partyAlternateNames = emptyList();
}
