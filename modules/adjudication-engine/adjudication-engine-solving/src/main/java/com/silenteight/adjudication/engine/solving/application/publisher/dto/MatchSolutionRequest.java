package com.silenteight.adjudication.engine.solving.application.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.Feature;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
public class MatchSolutionRequest implements Serializable {

  private static final long serialVersionUID = -4897354868083955120L;

  @Getter
  private final long alertId;

  @Getter
  private final long matchId;

  private final String policyName;

  private final List<String> featureNames;

  private final List<String> featureVectors;

  public BatchSolveFeaturesRequest mapToBatchSolveFeaturesRequest() {
    return BatchSolveFeaturesRequest.newBuilder()
        .setPolicyName(policyName)
        .setFeatureCollection(createFeatureCollection())
        .addFeatureVectors(createFeatureVector())
        .build();
  }

  private FeatureVector createFeatureVector() {
    return FeatureVector.newBuilder()
        .addAllFeatureValue(featureVectors)
        .build();
  }

  private FeatureCollection createFeatureCollection() {
    return FeatureCollection.newBuilder()
        .addAllFeature(createFeatures())
        .build();
  }

  private List<Feature> createFeatures() {
    return featureNames
        .stream()
        .map(featureName -> Feature.newBuilder().setName(featureName).build())
        .collect(toList());
  }
}
