package com.silenteight.agent.common.dictionary;

import lombok.Value;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableList;

@Value
public class MultipleValues {

  List<String> values;

  MultipleValues(String line) {
    this.values = stream(line.split(";")).collect(toUnmodifiableList());
  }
}
