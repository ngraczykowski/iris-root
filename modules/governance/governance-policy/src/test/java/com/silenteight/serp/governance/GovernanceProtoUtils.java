package com.silenteight.serp.governance;

import com.silenteight.solving.api.v1.BatchSolveFeaturesRequest;
import com.silenteight.solving.api.v1.Feature;
import com.silenteight.solving.api.v1.FeatureCollection;
import com.silenteight.solving.api.v1.FeatureVector;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class GovernanceProtoUtils {

  public static BatchSolveFeaturesRequest solveFeaturesRequest(
      String policyName,
      FeatureCollection featureCollection,
      FeatureVector... featureVectors) {

    return BatchSolveFeaturesRequest.newBuilder()
        .setPolicyName(policyName)
        .setFeatureCollection(featureCollection)
        .addAllFeatureVectors(asList(featureVectors))
        .build();
  }

  public static FeatureCollection featureCollection(String... featureNames) {
    List<Feature> features = Arrays.stream(featureNames)
        .map(GovernanceProtoUtils::feature)
        .collect(toList());

    return FeatureCollection.newBuilder()
        .addAllFeature(features)
        .build();
  }

  public static FeatureVector featureVector(String... featureValues) {
    return FeatureVector.newBuilder()
        .addAllFeatureValue(asList(featureValues))
        .build();
  }

  private static Feature feature(String featureName) {
    return Feature.newBuilder()
        .setName(featureName)
        .build();
  }
}
