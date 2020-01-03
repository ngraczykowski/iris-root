package com.silenteight.sens.webapp.backend.rest.decisiontree;

import com.silenteight.sens.webapp.backend.domain.decisiontree.DecisionTreeService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DecisionTreeRestConfiguration {

  @Bean
  DecisionTreeFacade decisionTreeFacade(DecisionTreeService service) {
    return new DecisionTreeFacade(service);
  }
}
