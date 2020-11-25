package com.silenteight.serp.governance.featurevector.dto;

import lombok.Value;

import com.silenteight.proto.serp.v1.alert.FeatureGroupVector;
import com.silenteight.proto.serp.v1.alert.VectorValue;

import com.google.protobuf.ByteString;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
public class StoreFeatureVectorsRequest {

  private final ByteString featuresSignature;
  private final List<Vector> vectors;

  public static StoreFeatureVectorsRequest of(
      ByteString featuresSignature,
      Collection<FeatureGroupVector> vectors) {

    return new StoreFeatureVectorsRequest(
        featuresSignature,
        vectors.stream().map(Vector::of).collect(toList()));
  }

  @Value
  public static class Vector {

    ByteString vectorSignature;
    List<VectorValue> values;

    static Vector of(FeatureGroupVector featureGroupVector) {
      return new Vector(
          featureGroupVector.getVectorSignature(),
          featureGroupVector.getValuesList());
    }
  }
}
