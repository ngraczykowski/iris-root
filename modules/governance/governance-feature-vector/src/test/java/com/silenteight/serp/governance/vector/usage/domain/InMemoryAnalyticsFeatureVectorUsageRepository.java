package com.silenteight.serp.governance.vector.usage.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.common.signature.Signature;

import java.util.Optional;

public class InMemoryAnalyticsFeatureVectorUsageRepository
    extends BasicInMemoryRepository<FeatureVectorUsage>
    implements AnalyticsFeatureVectorUsageRepository {

  @Override
  public Optional<FeatureVectorUsage> findByVectorSignature(
      Signature vectorSignature) {
    return stream().filter(fv -> fv.getVectorSignature().equals(vectorSignature)).findFirst();
  }
}
