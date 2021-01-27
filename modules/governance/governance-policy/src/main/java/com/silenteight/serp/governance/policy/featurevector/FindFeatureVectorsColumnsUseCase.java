package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsColumnsDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class FindFeatureVectorsColumnsUseCase {

  @NonNull
  private final FeatureVectorService featureVectorService;

  public FeatureVectorsColumnsDto activate() {
    List<String> columns = featureVectorService
        .getUniqueFeatureNames()
        .stream()
        .sorted()
        .collect(toList());

    return FeatureVectorsColumnsDto.builder()
        .columns(columns)
        .build();
  }
}
