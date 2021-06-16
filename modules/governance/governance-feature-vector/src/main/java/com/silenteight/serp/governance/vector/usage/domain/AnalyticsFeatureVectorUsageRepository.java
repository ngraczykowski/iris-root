package com.silenteight.serp.governance.vector.usage.domain;

import com.silenteight.serp.governance.common.signature.Signature;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AnalyticsFeatureVectorUsageRepository extends Repository<FeatureVectorUsage, Long> {

  FeatureVectorUsage save(FeatureVectorUsage featureVectorUsage);

  Optional<FeatureVectorUsage> findByVectorSignature(Signature vectorSignature);
}
