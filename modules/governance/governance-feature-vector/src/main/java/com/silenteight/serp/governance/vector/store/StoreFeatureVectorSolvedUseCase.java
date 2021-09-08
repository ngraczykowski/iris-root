package com.silenteight.serp.governance.vector.store;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.serp.governance.common.signature.Signature;
import com.silenteight.serp.governance.vector.domain.FeatureVectorService;
import com.silenteight.serp.governance.vector.usage.domain.UsageService;
import com.silenteight.solving.api.v1.Feature;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

import com.google.protobuf.ByteString;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Slf4j
public class StoreFeatureVectorSolvedUseCase {

  @NonNull
  private final FeatureVectorService featureVectorService;

  @NonNull
  private final UsageService usageService;

  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;

  @Transactional
  public void activate(List<FeatureVectorSolvedEvent> event) {
    Map<CanonicalFeatureVector, Long> distinctFeatureVectors = event
        .stream()
        .map(this::getCanonicalFeatureVector)
        .collect(groupingBy(Function.identity(), counting()));

    distinctFeatureVectors.keySet().forEach(featureVectorService::storeUniqueFeatureVector);
    distinctFeatureVectors.forEach(usageService::markAsUsed);
  }

  private CanonicalFeatureVector getCanonicalFeatureVector(FeatureVectorSolvedEvent event) {
    List<String> featureNames = getFeatureNames(event);
    List<String> featureValues = new ArrayList<>(event.getFeatureVector().getFeatureValueList());

    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromNamesAndValues(featureNames, featureValues);

    logIncompatibleSignature(canonicalFeatureVector.getVectorSignature(),
        event.getFeatureVectorSignature());
    return canonicalFeatureVector;
  }

  private static List<String> getFeatureNames(FeatureVectorSolvedEvent event) {
    return event
        .getFeatureCollection()
        .getFeatureList()
        .stream()
        .map(Feature::getName)
        .collect(toList());
  }

  private void logIncompatibleSignature(
      Signature canonicalSignature,
      ByteString requestSignature) {

    if (!canonicalSignature.asString().equals(requestSignature.toStringUtf8())) {
      log.warn("Incompatible FV Signature={}", canonicalSignature);
    }
  }
}
