package com.silenteight.adjudication.engine.solving.application.process;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.application.publisher.GovernanceMatchPublisher;
import com.silenteight.adjudication.engine.solving.domain.AlertSolving;
import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;

//TODO change name after when we'll know the context and what that does

@RequiredArgsConstructor
public class SomethingSolution {

  private final GovernanceMatchPublisher governanceProvider;
  private final AlertSolvingRepository alertSolvingRepository;

  //TODO Governence resolved alert
  public void updateResolution(Object o) {
    final AlertSolving alertSolvingModel = this.alertSolvingRepository.get(null);
    if (!alertSolvingModel.isEmpty()) {

      alertSolvingModel.updateMatches(null);
      this.alertSolvingRepository.save(alertSolvingModel);

      // Generate comments ???????
      //      if (alertSolvingModel.isAlertMatchSolved()) {
      //
      //        governanceProvider.send(null);
      //      }

    }
  }
}