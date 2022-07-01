/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.listener;

import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.event.AlertSolvedEvent;

import org.springframework.context.ApplicationEventPublisher;

public class ApplicationEventPublisherMock implements ApplicationEventPublisher {

  private final RemoveProcessedAlertListener removeProcessedAlertListener;

  public ApplicationEventPublisherMock(AlertSolvingRepository alertSolvingRepository) {
    this.removeProcessedAlertListener = new RemoveProcessedAlertListener(alertSolvingRepository);
  }

  @Override
  public void publishEvent(Object event) {
    removeProcessedAlertListener.onProcessedAlert((AlertSolvedEvent) event);
  }
}
