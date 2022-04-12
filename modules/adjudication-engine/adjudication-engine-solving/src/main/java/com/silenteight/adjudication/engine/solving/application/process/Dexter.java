package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.application.publisher.RecommendationPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

@RequiredArgsConstructor
public class Dexter {

  private final RecommendationPublisher recommendationPublisher;
  private final AlertSolvingRepository alertSolvingRepository;


  public void updateAlertSolutions(Object o) {
    // Update alert received solution.
    // Generate Comment for alert

    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(null);

    if (!alertSolvingModel.isEmpty()) {

      alertSolvingModel.updateFeatureMatches(null);
      this.alertSolvingRepository.save(alertSolvingModel);

      // I don't know that the same method should indicate about completeness ????
      if (alertSolvingModel.areAlertsSolved()) {

        // Verify if timout reached and update status ??
        // Send recommendation notification to topic (two listeners storing, sending cmapi)
        this.recommendationPublisher.publish(null);
      }
    }


  }
}
