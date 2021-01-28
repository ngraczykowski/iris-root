package com.silenteight.serp.governance.common.signature;

import lombok.RequiredArgsConstructor;

import com.google.protobuf.ByteString;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class CanonicalFeatureVectorFactory {

  private final SignatureCalculator signatureCalculator;

  public CanonicalFeatureVector fromNamesAndValues(
      List<String> featureNames, List<String> featureValues) {

    Iterator<String> keyIterator = featureNames.iterator();
    Iterator<String> valueIterator = featureValues.iterator();

    TreeMap<String, String> sortedMap = range(0, featureNames.size())
        .boxed()
        .collect(toMap(
            i -> keyIterator.next(),
            i -> valueIterator.next(),
            (o1, o2) -> o1,
            TreeMap::new));

    return asCanonicalFeatureVector(sortedMap);
  }

  private CanonicalFeatureVector asCanonicalFeatureVector(TreeMap<String,String> sortedMap) {
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