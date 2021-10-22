package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class ResourcesReaderTest {

  @Test
  void shouldReadAllSingleValues() {
    Set<String> lines = ResourcesReader.readLinesAsSet(
        ResourcesReaderTest.class.getResourceAsStream("/resourcereader/single_values.txt"));

    assertThat(lines).hasSize(3);
    assertThat(lines).containsExactlyInAnyOrder("ALA", "MA", "KOTA");
  }

  @Test
  void shouldReadAllMultipleValues() {
    Set<Set<String>> lines = ResourcesReader.readMultipleValues(
        ResourcesReaderTest.class.getResourceAsStream("/resourcereader/multiple_values.txt"));

    assertThat(lines).hasSize(3);
    assertThat(lines).containsExactlyInAnyOrder(
        Set.of("ALA", "OLA"),
        Set.of("MA"),
        Set.of("KOTA", "PSA", "KROWY"));
  }

}
