package com.silenteight.fab.dataprep.domain;

import com.silenteight.fab.dataprep.domain.feature.Feature;
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class FeedingService {

  private final List<Feature> features;

  FeedingService(List<Feature> features) {
    if (features.isEmpty()) {
      throw new IllegalStateException("There are no features enabled.");
    }
    this.features = features;
  }

  void createFeatureInputs(FeatureInputsCommand featureInputsCommand) {
    features.forEach(feature -> feature.createFeatureInput(featureInputsCommand));
  }
}
