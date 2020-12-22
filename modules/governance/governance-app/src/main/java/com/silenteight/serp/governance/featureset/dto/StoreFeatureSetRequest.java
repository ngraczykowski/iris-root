package com.silenteight.serp.governance.featureset.dto;

import lombok.Getter;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static java.util.stream.Collectors.toMap;

public class StoreFeatureSetRequest {

  @Getter
  @NotNull
  private final Set<FeatureSetToStoreDto> featureSets;

  public StoreFeatureSetRequest(@NotNull Set<FeatureSetToStoreDto> featureSets) {
    verify(featureSets);

    this.featureSets = featureSets;
  }

  private static void verify(Set<FeatureSetToStoreDto> featureSets) {
    if (!featuresAreSameForEverySignature(featureSets)) {
      throw new StoreFeatureSetRequestException("Features are different for same signatures");
    }
  }

  // TODO(bgulowaty): find smarter, more compact way
  private static boolean featuresAreSameForEverySignature(Set<FeatureSetToStoreDto> featureSets) {
    return featureSets
        .stream()
        .collect(
            toMap(FeatureSetToStoreDto::getFeatures, FeatureSetToStoreDto::getFeaturesSignature))
        .values()
        .stream()
        .distinct()
        .count() == featureSets.size();
  }

  private static class StoreFeatureSetRequestException extends RuntimeException {

    private static final long serialVersionUID = -281587015485314021L;

    StoreFeatureSetRequestException(String message) {
      super(message);
    }
  }
}
