package com.silenteight.adjudication.engine.analysis.matchsolution;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.adjudication.engine.analysis.matchsolution.dto.SaveMatchSolutionRequest;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeaturesFixtures {

  public static FeatureVector makeFeatureVector(String... values) {
    var builder = FeatureVector.newBuilder();

    for (String value : values) {
      builder.addFeatureValue(value);
    }

    return builder.build();
  }

  public static FeatureCollection makeFeatureCollection(String... features) {
    var builder = FeatureCollection.newBuilder();

    for (String feature : features) {
      builder.addFeatureBuilder().setName(feature);
    }

    return builder.build();
  }

  public static SaveMatchSolutionRequest makeSaveMatchSolutionRequest() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode reason = mapper.createObjectNode();
    reason.put("reason", "reason");
    return SaveMatchSolutionRequest
        .builder()
        .matchId(1)
        .analysisId(1)
        .solution("solution")
        .reason(reason)
        .build();
  }
}
