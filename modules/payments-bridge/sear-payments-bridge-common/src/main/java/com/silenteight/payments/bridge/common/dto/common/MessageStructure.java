package com.silenteight.payments.bridge.common.dto.common;

import lombok.Getter;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.*;
import static java.util.Arrays.asList;

public enum MessageStructure {
  STRUCTURED(TAG_BENE, TAG_ORGBANK, TAG_INSBANK, TAG_50F, TAG_RECEIVBANK, TAG_50K, TAG_59,
      TAG_ORIGINATOR),
  UNSUPPORTED(TAG_70, TAG_INSMN),
  UNSTRUCTURED();

  private static final Pattern COMPILED_PATTERN_UNSUPPORTED_TAG =
      Pattern.compile("[A-Za-z\\d]*");
  @Getter private final List<String> tags;

  MessageStructure(String... tags) {
    this.tags = asList(tags);
  }

  public static MessageStructure ofTag(String tag) {

    if (!COMPILED_PATTERN_UNSUPPORTED_TAG.matcher(tag).matches())
      return UNSUPPORTED;

    return Stream
        .of(MessageStructure.values())
        .filter(ms -> ms.tags.contains(tag))
        .findFirst()
        .orElse(UNSTRUCTURED);
  }

  public static List<String> getValues() {
    return Stream
        .of(MessageStructure.values())
        .map(MessageStructure::name)
        .collect(Collectors.toList());
  }

  public static boolean isMessageStructured(String tag) {
    return MessageStructure.STRUCTURED.equals(MessageStructure.ofTag(tag));
  }

}
