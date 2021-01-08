package com.silenteight.serp.governance;

import com.silenteight.proto.governance.v1.api.Feature;
import com.silenteight.proto.governance.v1.api.FeatureCollection;
import com.silenteight.proto.governance.v1.api.FeatureVector;
import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class GovernanceProtoUtils {

  public static GetSolutionsRequest solutionsRequest(
      FeatureCollection featureCollection, FeatureVector... featureVectors) {
    return GetSolutionsRequest.newBuilder()
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
