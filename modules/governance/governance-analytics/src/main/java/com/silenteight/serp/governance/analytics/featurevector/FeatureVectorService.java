package com.silenteight.serp.governance.analytics.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;
import com.silenteight.serp.governance.common.signature.Signature;

@RequiredArgsConstructor
public class FeatureVectorService {

  @NonNull
  private final AnalyticsFeatureVectorRepository analyticsFeatureVectorRepository;

  public void storeUniqueFeatureVector(CanonicalFeatureVector canonicalFeatureVector) {
    Signature vectorSignature = canonicalFeatureVector.getVectorSignature();
    if (isNewFeatureVector(vectorSignature))
      store(canonicalFeatureVector);
  }

  private boolean isNewFeatureVector(Signature vectorSignature) {
    return analyticsFeatureVectorRepository
        .findByVectorSignature(vectorSignature)
        .isEmpty();
  }

  private void store(CanonicalFeatureVector canonicalFeatureVector) {
    FeatureVector featureVector = new FeatureVector(
        canonicalFeatureVector.getVectorSignature(),
        canonicalFeatureVector.getNames(),
        canonicalFeatureVector.getValues());
    analyticsFeatureVectorRepository.save(featureVector);
  }
}
