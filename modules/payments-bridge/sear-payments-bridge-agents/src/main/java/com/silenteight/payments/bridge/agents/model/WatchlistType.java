package com.silenteight.payments.bridge.agents.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
public enum WatchlistType {
  ADDRESS("Other", "O", "OTHER"),
  COMPANY("Company", "C", "COMPANY", "CORPORATE"),
  INDIVIDUAL("Individual", "I", "INDIVIDUAL"),
  VESSEL("Vessel", "V", "VESSEL");

  @Getter
  private final String name;
  private final List<String> codes;

  WatchlistType(String name, String... codes) {
    this.name = name;
    this.codes = asList(codes);
  }

  public static WatchlistType ofCode(String code) {
    for (WatchlistType type : WatchlistType.values()) {
      if (code != null && type.codes.contains(code.toUpperCase()))
        return type;
    }
    throw new UnsupportedWatchlistType(code);
  }

  static class UnsupportedWatchlistType extends IllegalArgumentException {

    private static final long serialVersionUID = 1206843415273003009L;

    UnsupportedWatchlistType(String code) {
      super("Unsupported watchlist type: '" + code + "'");
    }
  }
}
