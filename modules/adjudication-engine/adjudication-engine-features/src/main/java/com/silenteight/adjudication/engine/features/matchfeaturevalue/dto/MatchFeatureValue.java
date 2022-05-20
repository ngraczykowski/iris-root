package com.silenteight.adjudication.engine.features.matchfeaturevalue.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.google.protobuf.Struct;

@Value
@Builder
public class MatchFeatureValue {

  long matchId;

  long agentConfigFeatureId;

  @NonNull
  String value;

  @NonNull
  Struct reason;
}
