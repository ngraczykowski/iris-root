/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.iris.qco.domain.QcoAlertService;
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;

import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QcoFacade {

  private final QcoAlertService qcoAlertService;

  public QcoRecommendationAlert process(QcoRecommendationAlert alert) {
    return Try.of(() -> qcoAlertService.extractAndProcessRecommendationAlert(alert))
        .onFailure(e ->
            log.error("Error occurred during QCO processing, batchId: {}", alert.batchId(), e))
        .getOrElse(alert);
  }
}
