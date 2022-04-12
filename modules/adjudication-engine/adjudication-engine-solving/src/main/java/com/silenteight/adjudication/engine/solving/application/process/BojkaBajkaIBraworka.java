package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.application.publisher.MatchesPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingFactory;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

@RequiredArgsConstructor
public class BojkaBajkaIBraworka {
  // Get alerts matches and features
  // Transform to InMemoryObject and store in memory (TTL)

  private final AlertSolvingFactory alertSolvingFactory;
  private final AlertSolvingRepository alertSolvingRepository;
  private final MatchesPublisher matchesPublisher;

  public void handle(
      final Object o
  ) {

    final AlertSolving alertSolvingModel = this.alertSolvingFactory.create(null);

    if (!alertSolvingModel.isEmpty()) {

      alertSolvingModel.updateFeatureMatches(null);

      this.alertSolvingRepository.save(alertSolvingModel);
      // I don't know that the same method should indicate about completeness ????
      if (alertSolvingModel.areAlertMatchesSolved()) {

        // Verify if timout reached and update status ??
        // Send recommendation notification to topic (two listeners storing, sending cmapi)

        this.matchesPublisher.publish(null);
      }
    }
  }

}
