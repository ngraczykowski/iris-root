package com.silenteight.adjudication.engine.solving.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AlertSolvingFactory {

  private final AlertSolvingRepository alertSolvingRepository;

  public AlertSolving create(final Long id) {

    final AlertSolving alertSolving = this.alertSolvingRepository.get(id);

    if (!alertSolving.isEmpty()) {
      return alertSolving;
    }
    final AlertSolving created = new AlertSolving(id);

    this.alertSolvingRepository.save(alertSolving);

    return created;
  }
}
