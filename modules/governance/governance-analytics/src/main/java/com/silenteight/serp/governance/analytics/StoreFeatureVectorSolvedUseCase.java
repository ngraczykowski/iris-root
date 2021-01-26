package com.silenteight.serp.governance.analytics;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.analytics.usage.UsageService;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.CanonicalFeatureVectorFactory;

import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class StoreFeatureVectorSolvedUseCase {

  @NonNull
  private final FeatureVectorService featureVectorService;

  @NonNull
  private final UsageService usageService;

  @NonNull
  private final CanonicalFeatureVectorFactory canonicalFeatureVectorFactory;

  @Transactional
  public void activate(Map<String, String> featureValuesByName, UUID stepId) {
    CanonicalFeatureVector canonicalFeatureVector = canonicalFeatureVectorFactory
        .fromMap(featureValuesByName);
    featureVectorService.storeUniqueFeatureVector(canonicalFeatureVector);
    usageService.markAsUsed(canonicalFeatureVector);
  }
}
