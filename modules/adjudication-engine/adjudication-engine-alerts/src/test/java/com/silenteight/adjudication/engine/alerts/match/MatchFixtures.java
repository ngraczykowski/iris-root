package com.silenteight.adjudication.engine.alerts.match;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class MatchFixtures {

  static MatchFacade inMemoryMatchFacade() {
    var matchRepository = new InMemoryMatchRepository();

    var createMatchesUseCase = new CreateMatchesUseCase(matchRepository);

    return new MatchFacade(createMatchesUseCase);
  }
}
