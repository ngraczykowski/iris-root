package com.silenteight.serp.governance.featureset;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

interface FeatureSetRepository extends Repository<FeatureSetEntity, Long> {

  FeatureSetEntity save(FeatureSetEntity featureSet);

  Optional<FeatureSetEntity> findByFeaturesSignature(String featuresSignature);

  Stream<FeatureSetEntity> findDistinctByFeaturesSignatureIn(
      Collection<String> featuresSignature);
}
