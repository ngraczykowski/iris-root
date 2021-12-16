package com.silenteight.payments.bridge.firco.recommendation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.events.ManualInvestigationReasonEvent;
import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.payments.bridge.firco.recommendation.port.IndexRecommendationPort;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;
import javax.transaction.Transactional;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_DAMAGED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@RequiredArgsConstructor
@Slf4j
@Service
class CreateRecommendationService implements CreateRecommendationUseCase {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final FilterAlertMessageUseCase alertMessageUseCase;
  private final RecommendationService recommendationService;
  private final IndexRecommendationPort indexRecommendationPort;
  private final ApplicationEventPublisher applicationEventPublisher;

  @Transactional
  @LogContext
  @Override
  public void create(BridgeSourcedRecommendation source) {
    var alertId = source.getAlertId();
    MDC.put("alertId", alertId.toString());
    indexRecommendationPort.send(source);

    var reason = RecommendationReason.valueOf(source.getReason());
    var recommendationId = recommendationService
        .createBridgeRecommendation(alertId, reason);
    applicationEventPublisher.publishEvent(new ManualInvestigationReasonEvent(reason.name()));

    var status = map(reason);
    if (alertMessageUseCase.isOutdated(source)) {
      alertMessageStatusUseCase.transitionAlertMessageStatus(alertId, status, UNDELIVERED);
    } else {
      var transited = alertMessageStatusUseCase
          .transitionAlertMessageStatus(alertId, status, PENDING);
      if (transited) {
        applicationEventPublisher.publishEvent(
            buildResponseCompleted(alertId, recommendationId.getId(), status));
      }
    }
    log.info("Alert bridge recommendation completed.");
  }

  private AlertMessageStatus map(RecommendationReason reason) {
    switch (reason) {
      case TOO_MANY_HITS:
        return RECOMMENDED;
      case OUTDATED:
        return REJECTED_OUTDATED;
      case DAMAGED:
        return REJECTED_DAMAGED;
      case QUEUE_OVERFLOWED:
        return REJECTED_OVERFLOWED;
      default:
        throw new IllegalArgumentException("Unmapped recommendation reason");
    }
  }

  @Transactional
  @LogContext
  @Override
  public void create(AdjudicationEngineSourcedRecommendation source) {
    var alertId = source.getAlertId();
    MDC.put("alertId", alertId.toString());
    indexRecommendationPort.send(source);

    MDC.put("recommendation",
        source.getRecommendation().getRecommendation().getRecommendedAction());

    var recommendationId = recommendationService
        .createAdjudicationRecommendation(alertId, source.getRecommendation());

    if (!alertMessageUseCase.isResolvedOrOutdated(source)) {
      var transited = alertMessageStatusUseCase
          .transitionAlertMessageStatus(alertId, RECOMMENDED, PENDING);
      if (transited) {
        applicationEventPublisher.publishEvent(
            buildResponseCompleted(alertId, recommendationId.getId(), RECOMMENDED));
      }
    }
    log.info("Alert adjudication recommendation completed.");
  }

  private ResponseCompleted buildResponseCompleted(
      UUID alertId, UUID recommendationId,
      AlertMessageStatus status) {
    return ResponseCompleted.newBuilder()
        .setAlert("alerts/" + alertId)
        .setStatus("alerts/" + alertId + "/status/" + status.name())
        .setRecommendation("alerts/" + alertId + "/recommendations/" + recommendationId)
        .build();
  }

}
