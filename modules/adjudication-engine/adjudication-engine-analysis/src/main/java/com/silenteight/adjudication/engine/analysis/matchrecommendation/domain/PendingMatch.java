package com.silenteight.adjudication.engine.analysis.matchrecommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Value
@Builder
public class PendingMatch {

  long matchId;

  FeatureVectorSolution matchSolution;

  ObjectNode matchContexts;
}
