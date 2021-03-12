package com.silenteight.serp.governance.analytics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.analytics.usage.UsageService;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;
import com.silenteight.solving.api.v1.Feature;
import com.silenteight.solving.api.v1.FeatureVectorSolvedEvent;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class StoreFeatureVectorSolvedUseCase {

  @NonNull
  private final FeatureVectorService featureVectorService;

  @NonNull
  private final UsageService usageService;

  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;

  @Transactional
  public void activate(FeatureVectorSolvedEvent event) {
    List<String> featureNames = event.getFeatureCollection()
        .getFeatureList()
        .stream()
        .map(Feature::getName)
        .collect(toList());
    List<String> featureValues = new ArrayList<>(event.getFeatureVector().getFeatureValueList());

    CanonicalFeatureVector canonicalFeatureVector =
        canonicalFeatureVectorFactory.fromNamesAndValues(featureNames, featureValues);

    featureVectorService.storeUniqueFeatureVector(canonicalFeatureVector);
    usageService.markAsUsed(canonicalFeatureVector);
  }
}
