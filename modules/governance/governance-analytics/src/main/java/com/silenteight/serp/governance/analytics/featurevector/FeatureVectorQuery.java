package com.silenteight.serp.governance.analytics.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.featurevector.FeatureNamesQuery;
import com.silenteight.serp.governance.policy.featurevector.FeatureVectorUsageQuery;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorWithUsageDto;

import java.util.List;
import java.util.stream.Stream;

import static com.google.common.base.Splitter.on;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@RequiredArgsConstructor
class FeatureVectorQuery implements FeatureVectorUsageQuery, FeatureNamesQuery {

  @NonNull
  private final AnalyticsFeatureVectorRepository analyticsFeatureVectorRepository;

  @Override
  public Stream<FeatureVectorWithUsageDto> getAllWithUsage() {
    return analyticsFeatureVectorRepository.findAllWithUsage()
        .map(FeatureVectorQuery::mapToDto);
  }

  private static FeatureVectorWithUsageDto mapToDto(FeatureVectorWithUsage featureVector) {
    return FeatureVectorWithUsageDto.builder()
        .signature(featureVector.getSignature())
        .usageCount(featureVector.getUsageCount())
        .names(split(featureVector.getNames()))
        .values(split(featureVector.getValues()))
        .build();
  }

  private static List<String> split(String elements) {
    return stream(on(',').split(elements).spliterator(), false)
        .collect(toList());
  }

  @Override
  public List<String> getUniqueFeatureNames() {
    return analyticsFeatureVectorRepository
        .findDistinctFeatureNames()
        .stream()
        .map(FeatureVectorQuery::split)
        .flatMap(List::stream)
        .distinct()
        .collect(toList());
  }
}
