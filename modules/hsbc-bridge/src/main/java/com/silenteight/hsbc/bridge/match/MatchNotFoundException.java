package com.silenteight.hsbc.bridge.match;

public class MatchNotFoundException extends RuntimeException {

  public MatchNotFoundException(long id) {
    super("Match has not been found, id: " + id);
  }
}
