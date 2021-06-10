package com.silenteight.adjudication.engine.analysis.agentexchange.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class MissingMatchFeatureChunk {

  private final List<MissingMatchFeature> missingMatchFeatures;

  public MissingMatchFeatureChunk(List<? extends MissingMatchFeature> missingMatchFeatures) {
    this.missingMatchFeatures = new ArrayList<>(missingMatchFeatures);
    this.missingMatchFeatures.sort(
        Comparator.comparing(MissingMatchFeature::getAgentConfig, String::compareToIgnoreCase));
  }

  public int getSize() {
    return missingMatchFeatures.size();
  }

  /**
   * Iterates over a list of {@link MissingMatchFeature}s sorted by agent config.
   */
  public void forEach(Consumer<MissingMatchFeature> consumer) {
    missingMatchFeatures.forEach(consumer);
  }
}
