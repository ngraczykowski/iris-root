package com.silenteight.hsbc.bridge.match;

public class MatchFacade {

  public MatchComposite getMatch(long id) {
    // TODO go to DB for matchRawData
    return new MatchComposite();
  }
}
