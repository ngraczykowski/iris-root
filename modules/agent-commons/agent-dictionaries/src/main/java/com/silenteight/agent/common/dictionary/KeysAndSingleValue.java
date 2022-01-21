package com.silenteight.agent.common.dictionary;

import lombok.Value;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toUnmodifiableList;

@Value
public class KeysAndSingleValue {

  List<String> keys;
  String value;

  KeysAndSingleValue(String line, boolean inverted) {
    var split = line.split("=");
    var left = split[0];
    var right = split[1];
    keys = splitMultiple(inverted ? right : left);
    value = inverted ? left : right;
  }

  private static List<String> splitMultiple(String text) {
    return stream(text.split(";")).collect(toUnmodifiableList());
  }
}
