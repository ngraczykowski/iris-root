package com.silenteight.agent.common.dictionary;

import lombok.Getter;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

class TestDictionary implements Dictionary {

  @Getter
  private final List<String> lines;

  TestDictionary(Stream<String> lines) {
    this(lines, s -> s);
  }

  TestDictionary(Stream<String> lines, UnaryOperator<String> operator) {
    this.lines = lines.map(operator).collect(toUnmodifiableList());
  }
}
