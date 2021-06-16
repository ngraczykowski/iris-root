package com.silenteight.serp.governance.vector.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;
import com.silenteight.serp.governance.vector.list.FeatureNamesQuery;
import com.silenteight.serp.governance.vector.list.FeatureVectorUsageQuery;
import com.silenteight.serp.governance.vector.list.ListVectorsQuery;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;
import javax.validation.Valid;

import static com.google.common.base.Splitter.on;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@RequiredArgsConstructor
class FeatureVectorQuery implements FeatureVectorUsageQuery, FeatureNamesQuery, ListVectorsQuery {

  @NonNull
  private final FeatureVectorRepository analyticsFeatureVectorRepository;

  @Override
  public Stream<FeatureVectorWithUsageDto> getAllWithUsage() {
    return analyticsFeatureVectorRepository
        .findAllWithUsage()
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
        .sorted()
        .collect(toList());
  }

  @Override
  @Transactional(readOnly = true)
  public FeatureVectorsDto list(@Valid Paging paging) {
    List<String> columns = getUniqueFeatureNames();

    List<FeatureVectorDto> featureVectorDtos = analyticsFeatureVectorRepository
        .findAllWithUsagePageable(paging.getSkip(), paging.getPageSize())
        .map(FeatureVectorQuery::mapToDto)
        .map(vector -> vector.standardize(columns))
        .collect(toList());

    return FeatureVectorsDto.builder()
        .columns(columns)
        .featureVectors(featureVectorDtos)
        .build();
  }

  @Override
  public int count() {
    return analyticsFeatureVectorRepository.countAll();
  }
}
