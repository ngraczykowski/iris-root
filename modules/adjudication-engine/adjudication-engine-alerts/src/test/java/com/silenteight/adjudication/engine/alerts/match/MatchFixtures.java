package com.silenteight.adjudication.engine.alerts.match;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchFixtures {

  public static MatchFacade inMemoryMatchFacade() {
    var matchRepository = new InMemoryMatchRepository();

    var createMatchesUseCase = new CreateMatchesUseCase(matchRepository);

    return new MatchFacade(createMatchesUseCase);
  }
}
