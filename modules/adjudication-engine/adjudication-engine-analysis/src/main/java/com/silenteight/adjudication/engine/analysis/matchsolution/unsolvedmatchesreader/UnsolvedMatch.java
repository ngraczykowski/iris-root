package com.silenteight.adjudication.engine.analysis.matchsolution.unsolvedmatchesreader;

import lombok.Value;

import com.silenteight.solving.api.v1.FeatureVector;

import java.util.Arrays;

@Value
class UnsolvedMatch {

  long matchId;

  String[] categoryValues;

  String[] featureValues;

  FeatureVector toFeatureVector() {
    return FeatureVector.newBuilder()
        .addAllFeatureValue(Arrays.asList(categoryValues))
        .addAllFeatureValue(Arrays.asList(featureValues))
        .build();
  }
}
