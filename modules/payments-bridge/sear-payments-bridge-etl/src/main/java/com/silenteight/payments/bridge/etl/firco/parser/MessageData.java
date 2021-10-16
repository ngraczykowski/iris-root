package com.silenteight.payments.bridge.etl.firco.parser;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MessageData {

  private final Map<String, String> tagValues;

  String get(String tagName) {
    return tagValues.get(tagName);
  }
}
