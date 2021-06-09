package com.silenteight.adjudication.engine.analysis.analysis.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import com.silenteight.solving.api.v1.FeatureCollection;

@Value
public class PolicyAndFeatureVectorElements {

  String policy;

  @Getter(AccessLevel.NONE)
  String[] categories;

  @Getter(AccessLevel.NONE)
  String[] features;

  public FeatureCollection toFeatureCollection() {
    var builder = FeatureCollection.newBuilder();

    addFeaturesFromArray(builder, categories);
    addFeaturesFromArray(builder, features);

    return builder.build();
  }

  private static void addFeaturesFromArray(
      FeatureCollection.Builder builder, String[] featureNames) {

    for (String featureName : featureNames) {
      builder.addFeatureBuilder().setName(featureName);
    }
  }
}
