package com.silenteight.hsbc.bridge.match;

public class MatchNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -5066545647044080064L;

  public MatchNotFoundException(String name) {
    super("Match has not been found, name: " + name);
  }
}
