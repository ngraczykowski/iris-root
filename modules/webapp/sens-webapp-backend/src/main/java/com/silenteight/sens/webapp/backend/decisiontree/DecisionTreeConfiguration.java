package com.silenteight.sens.webapp.backend.decisiontree;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DecisionTreeConfiguration {

  @Bean
  DecisionTreeFacade decisionTreeFacade(
      DecisionTreeQueryRepository decisionTreeQueryRepository,
      DecisionTreeRepository decisionTreeRepository) {

    return new DecisionTreeFacade(
        decisionTreeQuery(decisionTreeQueryRepository),
        decisionTreeService(decisionTreeRepository));
  }

  private static DecisionTreeQuery decisionTreeQuery(DecisionTreeQueryRepository repository) {
    return new DecisionTreeQuery(repository);
  }

  private static DecisionTreeService decisionTreeService(DecisionTreeRepository repository) {
    return new DecisionTreeService(repository);
  }

  DecisionTreeFacade decisionTreeFacadeForTest() {
    InMemoryDecisionTreeRepository repository = new InMemoryDecisionTreeRepository();
    return decisionTreeFacade(repository, repository);
  }
}
