package com.silenteight.serp.governance.vector.usage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;

@RequiredArgsConstructor
@Slf4j
public class UsageService {

  @NonNull
  private final AnalyticsFeatureVectorUsageRepository analyticsFeatureVectorUsageRepository;

  public void markAsUsed(CanonicalFeatureVector canonicalFeatureVector, long count) {
    FeatureVectorUsage featureVectorUsage = analyticsFeatureVectorUsageRepository
        .findByVectorSignature(canonicalFeatureVector.getVectorSignature())
        .orElseGet(() -> asFeatureVectorUsage(canonicalFeatureVector));

    log.debug("Feature vector (fv_signature={}) used {} times.",
              canonicalFeatureVector.getVectorSignature(), count);
    featureVectorUsage.markAsUsed(count);
    analyticsFeatureVectorUsageRepository.save(featureVectorUsage);
  }

  public long getUsageCount(CanonicalFeatureVector canonicalFeatureVector) {
    return analyticsFeatureVectorUsageRepository
        .findByVectorSignature(canonicalFeatureVector.getVectorSignature())
        .map(FeatureVectorUsage::getUsageCount)
        .orElse(0L);
  }

  private static FeatureVectorUsage asFeatureVectorUsage(
      CanonicalFeatureVector canonicalFeatureVector) {

    return new FeatureVectorUsage(canonicalFeatureVector.getVectorSignature(), 0L);
  }
}
