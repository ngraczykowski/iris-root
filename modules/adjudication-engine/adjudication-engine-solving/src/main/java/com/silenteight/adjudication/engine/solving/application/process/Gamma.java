package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.engine.solving.application.publisher.GovernancePublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

public class Gamma {

  private final GovernancePublisher governanceProvider;
  private final AlertSolvingRepository alertSolvingRepository;

  Gamma(
      GovernancePublisher governanceProvider,
      AlertSolvingRepository alertSolvingRepository) {
    this.governanceProvider = governanceProvider;
    this.alertSolvingRepository = alertSolvingRepository;
  }

  public void updateMatches(final Object o) {
    // Update matches received features.
    // Verify state of matches - if completed

    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(null);

    if (!alertSolvingModel.isEmpty()) {

      alertSolvingModel.updateMatchFeatureValues(null);
      this.alertSolvingRepository.save(alertSolvingModel);

      // I don't know that the same method should indicate about completeness ????
      if (alertSolvingModel.areAlertMatchesSolved()) {

        // Send to governance via queue (internal)
        // Create Governance internal queue listener and send to Gov
        // TODO
        this.governanceProvider.send(null);
      }
    }


  }
}
