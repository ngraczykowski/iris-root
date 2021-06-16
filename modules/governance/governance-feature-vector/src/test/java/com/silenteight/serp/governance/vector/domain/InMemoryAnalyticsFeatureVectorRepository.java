package com.silenteight.serp.governance.vector.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;
import com.silenteight.serp.governance.common.signature.Signature;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class InMemoryAnalyticsFeatureVectorRepository
    extends BasicInMemoryRepository<FeatureVector> implements FeatureVectorRepository {

  @Override
  public Optional<FeatureVector> findByVectorSignature(Signature vectorSignature) {
    return stream()
        .filter(fv -> fv.getVectorSignature().equals(vectorSignature))
        .findFirst();
  }

  @Override
  public Stream<FeatureVectorWithUsage> findAllWithUsage() {
    return stream()
        .map(InMemoryAnalyticsFeatureVectorRepository::mapToFeatureVectorWithUsage);
  }

  @Override
  public Stream<FeatureVectorWithUsage> findAllWithUsagePageable(long offset, int limit) {
    return findAllWithUsage().skip(offset).limit(limit);
  }

  private static final FeatureVectorWithUsage mapToFeatureVectorWithUsage(
      FeatureVector featureVector) {

    return TestFeatureVectorWithUsageDto.builder()
        .signature(featureVector.getSignatureAsString())
        .usageCount(0L)
        .names(join(featureVector.getNames()))
        .values(join(featureVector.getValues()))
        .build();
  }

  @Override
  public List<String> findDistinctFeatureNames() {
    return stream()
        .map(FeatureVector::getNames)
        .flatMap(List::stream)
        .distinct()
        .collect(toList());
  }

  @Override
  public int countAll() {
    return count();
  }

  private static String join(List<String> elements) {
    return elements
        .stream()
        .collect(joining(","));
  }

  @Value
  @Builder
  private static class TestFeatureVectorWithUsageDto implements FeatureVectorWithUsage {

    @NonNull
    String signature;

    @NonNull
    Long usageCount;

    @NonNull
    String names;

    @NonNull
    String values;
  }
}
