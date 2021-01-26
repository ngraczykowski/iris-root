package com.silenteight.serp.governance.common.signature;

import lombok.RequiredArgsConstructor;

import com.google.protobuf.ByteString;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
public class CanonicalFeatureVectorFactory {

  private final SignatureCalculator signatureCalculator;

  public CanonicalFeatureVector fromMap(Map<String,String> featureValuesByName) {
    TreeMap<String, String> sortedMap = new TreeMap<>(featureValuesByName);

    List<String> names = getSortedNames(sortedMap);
    List<String> values = getSortedValues(sortedMap);
    Signature signature = calculateSignature(names, values);

    return CanonicalFeatureVector.builder()
        .names(names)
        .values(values)
        .vectorSignature(signature)
        .build();
  }

  private List<String> getSortedNames(TreeMap<String, String> featureValuesByName) {
    return featureValuesByName.keySet().stream().collect(toUnmodifiableList());
  }

  private List<String> getSortedValues(TreeMap<String, String> featureValuesByName) {
    return featureValuesByName.values().stream().collect(toUnmodifiableList());
  }

  private Signature calculateSignature(List<String> names, List<String> values) {
    ByteString featuresSignature = signatureCalculator.calculateFeaturesSignature(names);
    ByteString vectorSignature = signatureCalculator
        .calculateVectorSignature(featuresSignature, values);
    return new Signature(vectorSignature);
  }
}