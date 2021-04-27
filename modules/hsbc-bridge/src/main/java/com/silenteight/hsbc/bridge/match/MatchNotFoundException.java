package com.silenteight.hsbc.bridge.match;

public class MatchNotFoundException extends RuntimeException {

  public MatchNotFoundException(String name) {
    super("Match has not been found, name: " + name);
  }
}
