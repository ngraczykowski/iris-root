package com.silenteight.serp.governance.vector.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;

@RequiredArgsConstructor
@Slf4j
public class FeatureVectorService {

  @NonNull
  private final FeatureVectorRepository analyticsFeatureVectorRepository;

  public void storeUniqueFeatureVector(CanonicalFeatureVector canonicalFeatureVector) {
    log.info("Storing a new FV: {}", canonicalFeatureVector);
    FeatureVector featureVector = new FeatureVector(
        canonicalFeatureVector.getVectorSignature(),
        canonicalFeatureVector.getNames(),
        canonicalFeatureVector.getValues());
    analyticsFeatureVectorRepository.saveIfNotExist(featureVector);
  }
}
