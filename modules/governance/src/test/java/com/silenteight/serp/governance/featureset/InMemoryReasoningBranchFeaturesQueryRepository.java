package com.silenteight.serp.governance.featureset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryReasoningBranchFeaturesQueryRepository implements
    ReasoningBranchFeaturesQueryRepository {

  private final Map<Long, ReasoningBranchFeaturesQuery> store = new HashMap<>();

  void store(long featureSetId, long featureVectorId, List<String> features) {
    store.put(featureVectorId, ReasoningBranchFeaturesQuery.builder()
        .featureSetId(featureSetId)
        .featureVectorId(featureVectorId)
        .features(features)
        .build());
  }

  @Override
  public Optional<ReasoningBranchFeaturesQuery> findByFeatureVectorId(long featureVectorId) {
    return Optional.ofNullable(store.get(featureVectorId));
  }
}
