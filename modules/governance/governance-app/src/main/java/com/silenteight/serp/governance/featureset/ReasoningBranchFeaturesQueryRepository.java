package com.silenteight.serp.governance.featureset;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface ReasoningBranchFeaturesQueryRepository extends
    Repository<ReasoningBranchFeaturesQuery, Long> {

  Optional<ReasoningBranchFeaturesQuery> findByFeatureVectorId(long featureVectorId);
}
