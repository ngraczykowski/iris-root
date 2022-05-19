package com.silenteight.bridge.core.recommendation.adapter.incoming.amqp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.recommendation.domain.RecommendationFacade;
import com.silenteight.bridge.core.recommendation.domain.command.ProceedDataRetentionOnRecommendationsCommand;
import com.silenteight.dataretention.api.v1.AlertsExpired;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class DataRetentionRabbitAmqpListener {

  private final RecommendationFacade facade;

  @RabbitListener(
      queues = "${amqp.recommendation.incoming.data-retention.alerts-expired-queue-name}",
      errorHandler = "recommendationAmqpErrorHandler"
  )
  public void subscribe(AlertsExpired alertsExpired) {
    var alertNames = alertsExpired.getAlertsList();
    log.info("Received AlertsExpired with [{}] alert names", alertNames.size());
    var command = new ProceedDataRetentionOnRecommendationsCommand(alertNames);
    facade.performDataRetention(command);
  }
}
