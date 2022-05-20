package com.silenteight.adjudication.engine.solving.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertSolvingFactory {

  private final AlertSolvingRepository alertSolvingRepository;

  public AlertSolving create(final Long alertId, final Long analysisId) {

    final AlertSolving alertSolving = this.alertSolvingRepository.get(alertId);

    if (!alertSolving.isEmpty()) {
      return alertSolving;
    }
    final AlertSolving created = new AlertSolving(alertId, "", "", analysisId);

    this.alertSolvingRepository.save(alertSolving);

    return created;
  }
}
