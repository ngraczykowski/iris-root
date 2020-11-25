package com.silenteight.serp.governance.featureset;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

class InMemoryFeatureSetRepository extends BasicInMemoryRepository<FeatureSetEntity>
    implements FeatureSetRepository {

  @Override
  public Optional<FeatureSetEntity> findByFeaturesSignature(String featuresSignature) {
    return stream()
        .filter(e -> e.getFeaturesSignature().equals(featuresSignature))
        .findAny();
  }

  @Override
  public Stream<FeatureSetEntity> findDistinctByFeaturesSignatureIn(
      Collection<String> featuresSignature) {
    return stream().filter(e -> featuresSignature.contains(e.getFeaturesSignature()));
  }
}
