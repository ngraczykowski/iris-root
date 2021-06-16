package com.silenteight.serp.governance.vector.usage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.signature.CanonicalFeatureVector;

@RequiredArgsConstructor
public class UsageService {

  @NonNull
  private final AnalyticsFeatureVectorUsageRepository analyticsFeatureVectorUsageRepository;

  public void markAsUsed(CanonicalFeatureVector canonicalFeatureVector) {
    FeatureVectorUsage featureVectorUsage = analyticsFeatureVectorUsageRepository
        .findByVectorSignature(canonicalFeatureVector.getVectorSignature())
        .orElseGet(() -> asFeatureVectorUsage(canonicalFeatureVector));

    featureVectorUsage.markAsUsed();

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
