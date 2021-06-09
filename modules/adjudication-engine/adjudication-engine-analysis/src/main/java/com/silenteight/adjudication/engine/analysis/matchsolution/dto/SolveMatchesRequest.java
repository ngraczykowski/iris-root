package com.silenteight.adjudication.engine.analysis.matchsolution.dto;

import lombok.Value;

import com.silenteight.solving.api.v1.FeatureCollection;

@Value
public class SolveMatchesRequest {

  long analysisId;

  String policy;

  FeatureCollection featureCollection;
}
