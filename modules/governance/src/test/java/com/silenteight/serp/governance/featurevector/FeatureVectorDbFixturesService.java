package com.silenteight.serp.governance.featurevector;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.alert.VectorValue;

import com.google.protobuf.ByteString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class FeatureVectorDbFixturesService {

  private final FeatureVectorRepository featureVectorRepository;

  public long storeFeatureVector(String featureSignature, String... values) {
    List<VectorValue> vectorValues = Arrays.stream(values)
        .map(v -> VectorValue.newBuilder().setTextValue(v).build())
        .collect(Collectors.toList());
    ByteString bytesFeatureSignature = ByteString.copyFromUtf8(featureSignature);

    FeatureVectorEntity featureVectorEntity = new FeatureVectorEntity(
        bytesFeatureSignature,
        bytesFeatureSignature,
        vectorValues
    );
    return featureVectorRepository.save(featureVectorEntity).getId();
  }
}
