package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateAnalysisOut {

  String name;
  String policy;
  String strategy;
}
