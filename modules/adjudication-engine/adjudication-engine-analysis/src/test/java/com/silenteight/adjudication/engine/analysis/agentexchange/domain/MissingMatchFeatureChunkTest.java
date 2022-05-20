package com.silenteight.adjudication.engine.analysis.agentexchange.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.function.Function.identity;
import static org.assertj.core.api.Assertions.*;

class MissingMatchFeatureChunkTest {

  @Test
  void shouldSortMissingFeatures() {
    var missingMatchFeatureChunk = new MissingMatchFeatureChunk(List.of(
        missingMatchFeature("agents/ZZZ", "features/ZZZ"),
        missingMatchFeature("agents/AAA", "features/XXX"),
        missingMatchFeature("agents/ZZZ", "features/AAA")));

    var result = new ArrayList<MissingMatchFeature>();
    missingMatchFeatureChunk.forEach(result::add);

    assertThat(result)
        .extracting(mmf -> mmf.getAgentConfig() + ":" + mmf.getFeature())
        .isSortedAccordingTo(comparing(identity(), String::compareToIgnoreCase));
  }

  MissingMatchFeature missingMatchFeature(String agentConfig, String feature) {
    return new MissingMatchFeature(
        current().nextLong(), current().nextLong(), current().nextLong(), agentConfig, feature,
        current().nextInt(1, 11));
  }
}
