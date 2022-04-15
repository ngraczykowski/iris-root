package com.silenteight.sens.webapp.logging;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public enum SensWebappMdcKeys {

  USERNAME("username");

  private final String key;

  public static List<String> getAllKeys() {
    return Stream.of(SensWebappMdcKeys.values())
        .map(SensWebappMdcKeys::getKey)
        .collect(toList());
  }
}
