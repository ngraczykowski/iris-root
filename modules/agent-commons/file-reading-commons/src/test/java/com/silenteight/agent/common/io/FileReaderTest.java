package com.silenteight.agent.common.io;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.*;

class FileReaderTest {

  @Test
  void shouldReadMultipleValues() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readMultipleValues.txt");
    var path = url.getPath();

    //when
    var values = FileReader.readMultipleValues(path);

    //then
    assertThat(values)
        .containsExactlyInAnyOrder(Set.of("VALUE1", "VALUE2", "VALUE3"));
  }

  @Test
  void shouldReadSingleKeyMultipleValues() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readSingleKeyMultipleValues.txt");
    var path = url.getPath();

    //when
    var keyAndValues = FileReader.readSingleKeyMultipleValues(path);

    //then
    assertThat(keyAndValues.get("KEY"))
        .containsExactlyInAnyOrder("VALUE1", "VALUE2", "VALUE3");
  }

  @Test
  void shouldReadMultipleKeyMultipleValues() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readMultipleKeyMultipleValues.txt");
    var path = url.getPath();

    //when
    var keyAndValues = FileReader.readMultipleKeyMultipleValues(path);

    //then
    assertThat(keyAndValues.get(Set.of("KEY1", "KEY2", "KEY3")))
        .containsExactlyInAnyOrder("VALUE1", "VALUE2", "VALUE3");
  }

  @Test
  void shouldReadRegexpPatternsAsList() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readRegexpPatternsAsList.txt");
    var path = url.getPath();
    var matchingValues = Set.of("123", "ABC", "REGEX");
    var notMatchingValues = Set.of("!@#$%", "ABC123", "   ");

    //when
    var lines = FileReader.readRegexpPatternsAsList(path);
    var regexPatterns = lines.stream()
        .map(Pattern::compile)
        .collect(toSet());

    var matchingValuesResult = matchingValues.stream()
        .allMatch(value -> match(value, regexPatterns));

    var notMatchingValuesResult = notMatchingValues.stream()
        .allMatch(value -> match(value, regexPatterns));

    //then
    assertThat(matchingValuesResult).isTrue();
    assertThat(notMatchingValuesResult).isFalse();
  }

  private boolean match(String value, Set<Pattern> patterns) {
    for (Pattern pattern : patterns) {
      var matcher = pattern.matcher(value);
      if (matcher.matches()) {
        return true;
      }
    }
    return false;
  }

  @Test
  void shouldReadLinesAsSet() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readLinesAsSet.txt");
    var path = url.getPath();

    //when
    var values = FileReader.readLinesAsSet(path);

    //then
    assertThat(values)
        .containsExactlyInAnyOrder("VALUE1", "VALUE2", "VALUE3");
  }

  @Test
  void shouldReadLinesAsStream() {
    //given
    var url = FileReaderTest.class.getResource("/filereader/readLinesAsStream.txt");
    var path = url.getPath();

    //when
    var stream = FileReader.readLinesAsStream(path);

    //then
    var values = stream
        .map(String::toUpperCase)
        .collect(toSet());

    assertThat(values)
        .containsExactlyInAnyOrder("VALUE1", "VALUE2", "VALUE3");
  }
}