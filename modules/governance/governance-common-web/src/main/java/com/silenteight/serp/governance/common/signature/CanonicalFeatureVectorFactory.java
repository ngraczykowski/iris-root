package com.silenteight.serp.governance.common.signature;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.ByteString;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
@Slf4j
public class CanonicalFeatureVectorFactory {

  private final SignatureCalculator signatureCalculator;

  public CanonicalFeatureVector fromNamesAndValues(
      List<String> featureNames, List<String> featureValues) {

    checkIfInputIsValid(featureNames, featureValues);

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

  private CanonicalFeatureVector asCanonicalFeatureVector(TreeMap<String, String> sortedMap) {
    List<String> names = getSortedNames(sortedMap);
    List<String> values = getSortedValues(sortedMap);
    Signature signature = calculateSignature(names, values);

    log.debug("Generating CanonicalFeatureVector (features={}, signature={})",
              sortedMap, signature);
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

  private void checkIfInputIsValid(List<String> featureNames, List<String> featureValues) {
    checkIfInputHasData(featureNames, featureValues);
    checkIfInputHasEmptyFields(featureNames);
    checkIfInputHasEmptyFields(featureValues);
    checkIfFeatureNameIsCorrect(featureNames);
    checkIfCorrectAgentNameAndFeatureCount(featureNames, featureValues);
  }

  private void checkIfFeatureNameIsCorrect(List<String> agentsList) {
    HashSet<String> uniqueNames = new HashSet<>();
    uniqueNames.addAll(agentsList);
    if (agentsList.size() != uniqueNames.size())
      throw new InvalidInputException("Duplicated agents names");
  }

  private void checkIfCorrectAgentNameAndFeatureCount(
      List<String> featureNames, List<String> featureValues) {
    if (!(featureNames.size() == featureValues.size()))
      throw new InvalidInputException(
          "Mismatch between agents name count and feature vectors count");
  }

  private void checkIfInputHasEmptyFields(List<String> featureFields) {
    boolean isAnyFieldEmpty = isAnyFieldEmpty(featureFields);
    if (isAnyFieldEmpty)
      throw new InvalidInputException("Some fields are empty");
  }

  private void checkIfInputHasData(List<String> featureNames, List<String> featureValues) {
    if (featureNames.isEmpty())
      throw new InvalidInputException("Missing data: featureNames are empty");

    if (featureValues.isEmpty())
      throw new InvalidInputException("Missing data: featureValues are empty");
  }

  private boolean isAnyFieldEmpty(List<String> featureFields) {
    return featureFields.stream()
        .anyMatch(String::isBlank);
  }
}
