package com.silenteight.payments.bridge.common.dto.common;

import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public enum MessageStructure {
  STRUCTURED("BENE", "BENE", "ORGBANK", "INSBANK", "50F", "RECIVBANK", "50K", "59", "ORIGNATOR"),
  UNSUPPORTED("70", "INSMN"),
  UNSTRUCTURED();

  @Getter
  private final List<String> tags;

  MessageStructure(String... tags) {
    this.tags = asList(tags);
  }

  public static MessageStructure ofTag(String tag) {
    return Stream.of(MessageStructure.values())
        .filter(ms -> ms.tags.contains(tag))
        .findFirst()
        .orElse(UNSTRUCTURED);
  }

  public static List<String> getValues() {
    return Stream.of(MessageStructure.values())
        .map(MessageStructure::name)
        .collect(Collectors.toList());
  }
}
