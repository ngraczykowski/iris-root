package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.application.publisher.GovernancePublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

//TODO change name after when we'll know the context and what that does

@RequiredArgsConstructor
public class SomethingSolution {

  private final GovernancePublisher governanceProvider;
  private final AlertSolvingRepository alertSolvingRepository;

  public void updateResolution(Object o) {
    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(null);
    if (!alertSolvingModel.isEmpty()) {

      alertSolvingModel.updateMatches(null);
      this.alertSolvingRepository.save(alertSolvingModel);

      if (alertSolvingModel.areAlertMatchesSolved()) {

        governanceProvider.send(null);
      }

    }
  }
}
