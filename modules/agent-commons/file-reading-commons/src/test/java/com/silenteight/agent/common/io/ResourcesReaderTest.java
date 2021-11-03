package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResourcesReaderTest {

  @Test
  void shouldReadAllSingleValues() {
    Set<String> lines = ResourcesReader.readLinesAsSet(
        requireNonNull(
            ResourcesReaderTest.class.getResourceAsStream("/resourcereader/single_values.txt")));

    assertThat(lines).hasSize(3);
    assertThat(lines).containsExactlyInAnyOrder("ALA", "MA", "KOTA");
  }

  @Test
  void shouldReadAllMultipleValues() {
    Set<Set<String>> lines = ResourcesReader.readMultipleValues(
        requireNonNull(
            ResourcesReaderTest.class.getResourceAsStream("/resourcereader/multiple_values.txt")));

    assertThat(lines).hasSize(3);
    assertThat(lines).containsExactlyInAnyOrder(
        Set.of("ALA", "OLA"),
        Set.of("MA"),
        Set.of("KOTA", "PSA", "KROWY"));
  }

  @Test
  void shouldReadAllSingleValuesWithProvidedFiltersAndTransformers() {

    //given
    Predicate<String> ignoreLineStartingWithPlus = s -> !s.startsWith("+");
    UnaryOperator<String> toUpperCase = String::toUpperCase;
    var transformers = List.of(toUpperCase);
    var filters = List.of(ignoreLineStartingWithPlus);

    //when
    Set<String> lines = ResourcesReader.readLinesAsSet(
        requireNonNull(ResourcesReaderTest.class.getResourceAsStream(
            "/resourcereader/custom_single_values.txt")),
        filters,
        transformers);

    //then
    assertThat(lines).containsOnly(
        "ALA", "MA", "KOTA");
  }

  @Test
  void shouldThrowNPEWhenParamIsNull() {

    //when
    Executable result = () -> ResourcesReader.readLinesAsSet(null);

    //then
    assertThrows(NullPointerException.class, result);
  }
}
