package com.silenteight.adjudication.engine.solving.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Value
public final class FeatureSolution implements Serializable {

  private static final long serialVersionUID = 554276097928977893L;
  String featureName;
  String solution;
  String reason;
}
