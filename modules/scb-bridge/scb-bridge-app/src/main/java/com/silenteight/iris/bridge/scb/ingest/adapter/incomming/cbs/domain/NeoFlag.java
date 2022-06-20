/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static java.util.Arrays.stream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum NeoFlag {
  NEW("N"),
  EXISTING("E"),
  OBSOLETE("O");

  @Getter
  private final String code;

  public static Optional<NeoFlag> parse(@NonNull String flag) {
    return stream(NeoFlag.values())
        .filter(neoFlag -> StringUtils.equalsIgnoreCase(flag, neoFlag.code))
        .findFirst();
  }
}
