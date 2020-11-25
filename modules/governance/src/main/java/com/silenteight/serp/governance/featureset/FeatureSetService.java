package com.silenteight.serp.governance.featureset;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.featureset.dto.FeatureSetToStoreDto;
import com.silenteight.serp.governance.featureset.dto.StoreFeatureSetRequest;

import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@RequiredArgsConstructor
@Slf4j
public class FeatureSetService {

  private final FeatureSetRepository repository;

  @Transactional
  public void storeFeatureSet(@NonNull StoreFeatureSetRequest request) {
    Set<String> signatures = request
        .getFeatureSets()
        .stream()
        .map(FeatureSetToStoreDto::getFeaturesSignatureAsString)
        .collect(toSet());

    repository
        .findDistinctByFeaturesSignatureIn(signatures)
        .map(FeatureSetEntity::getFeaturesSignature)
        .forEach(signatures::remove);

    request
        .getFeatureSets()
        .stream()
        .filter(dto -> signatures.contains(dto.getFeaturesSignatureAsString()))
        .peek(dto -> log.info("Saving new feature set: dto={}", dto))
        .map(FeatureSetEntity::new)
        .forEach(repository::save);
  }
}
