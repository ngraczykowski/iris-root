package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.GovernancePublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.MatchesPublisher;
import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingFactory;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProcessConfiguration {

  @Bean
  BojkaBajkaIBraworka bojkaBajkaIBraworka(
      AlertSolvingRepository alertSolvingRepository,
      AlertSolvingFactory alertSolvingFactory,
      MatchesPublisher matchesPublisher
  ) {
    return new BojkaBajkaIBraworka(
        alertSolvingFactory, alertSolvingRepository,
        matchesPublisher
    );
  }


  @Bean
  Dexter dexter(
      AlertSolvingRepository alertSolvingRepository,
      RecommendationPublisher recommendationPublisher) {
    return new Dexter(recommendationPublisher, alertSolvingRepository);
  }

  @Bean
  Gamma gamma(
      final GovernancePublisher governanceProvider, AlertSolvingRepository alertSolvingRepository) {
    return new Gamma(governanceProvider, alertSolvingRepository);
  }

  @Bean
  SomethingSolution somethingSolution(
      final GovernancePublisher governanceProvider,
      final AlertSolvingRepository alertSolvingRepository
  ) {
    return new SomethingSolution(governanceProvider, alertSolvingRepository);
  }
}
