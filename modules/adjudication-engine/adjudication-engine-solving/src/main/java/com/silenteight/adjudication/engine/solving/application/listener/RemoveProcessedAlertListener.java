/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.domain.AlertSolvingRepository;
import com.silenteight.adjudication.engine.solving.domain.event.AlertSolvedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class RemoveProcessedAlertListener {

  private final AlertSolvingRepository alertSolvingRepository;

  @EventListener(classes = AlertSolvedEvent.class)
  void onProcessedAlert(AlertSolvedEvent event) {
    log.debug("Received object removal event from cache: {}", event.alertId());
    alertSolvingRepository.remove(event.alertId());
  }
}
