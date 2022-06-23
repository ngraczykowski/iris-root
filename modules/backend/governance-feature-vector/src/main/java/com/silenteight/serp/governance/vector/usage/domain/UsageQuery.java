package com.silenteight.serp.governance.vector.usage.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.vector.domain.exception.FeatureVectorNotFoundException;
import com.silenteight.serp.governance.vector.usage.statistics.UsageCountQuery;

import static com.silenteight.serp.governance.common.signature.Signature.fromBase64;

@RequiredArgsConstructor
class UsageQuery implements UsageCountQuery {

  @NonNull
  private final AnalyticsFeatureVectorUsageRepository repository;

  @Override
  public long getUsageCount(String signature) {
    return repository
        .findByVectorSignature(fromBase64(signature))
        .orElseThrow(() -> new FeatureVectorNotFoundException(signature))
        .getUsageCount();
  }
}
