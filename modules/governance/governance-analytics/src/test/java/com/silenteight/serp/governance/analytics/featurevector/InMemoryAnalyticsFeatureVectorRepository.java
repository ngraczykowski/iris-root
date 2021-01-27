package com.silenteight.serp.governance.analytics.featurevector;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.common.signature.Signature;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class InMemoryAnalyticsFeatureVectorRepository
    extends BasicInMemoryRepository<FeatureVector> implements AnalyticsFeatureVectorRepository {

  @Override
  public Optional<FeatureVector> findByVectorSignature(
      Signature vectorSignature) {
    return stream().filter(fv -> fv.getVectorSignature().equals(vectorSignature)).findFirst();
  }

  @Override
  public Stream<FeatureVector> findAll() {
    return stream();
  }

  @Override
  public List<String> findDistinctFeatureNames() {
    return findAll()
        .map(FeatureVector::getNames)
        .flatMap(List::stream)
        .distinct()
        .collect(toList());
  }
}
