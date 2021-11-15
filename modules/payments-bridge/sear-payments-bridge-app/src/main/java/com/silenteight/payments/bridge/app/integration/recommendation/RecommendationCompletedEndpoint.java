package com.silenteight.payments.bridge.app.integration.recommendation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.AdjudicationRecommendationCompletedEvent;
import com.silenteight.payments.bridge.event.RecommendationCompletedEvent.BridgeRecommendationCompletedEvent;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessageStatusUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.port.FilterAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.proto.payments.bridge.internal.v1.event.ResponseCompleted;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import javax.transaction.Transactional;

import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.RECOMMENDED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_DAMAGED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OUTDATED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus.REJECTED_OVERFLOWED;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.PENDING;
import static com.silenteight.payments.bridge.firco.alertmessage.model.DeliveryStatus.UNDELIVERED;

@RequiredArgsConstructor
@Slf4j
@MessageEndpoint
class RecommendationCompletedEndpoint {

  private final AlertMessageStatusUseCase alertMessageStatusUseCase;
  private final FilterAlertMessageUseCase alertMessageUseCase;
  private final CreateRecommendationUseCase createRecommendationUseCase;
  private final ApplicationEventPublisher applicationEventPublisher;

  private final List<Consumer<RecommendationCompletedEvent>> eventConsumers =
      List.of(
          new AdjudicationRecommendationCommand(),
          new BridgeRecommendationCommand()
      );

  @Order(2)
  @ServiceActivator(inputChannel = RecommendationCompletedEvent.CHANNEL)
  @Transactional
  @LogContext
  void accept(RecommendationCompletedEvent event) {
    MDC.put("alertId", event.getAlertId().toString());
    eventConsumers.forEach(c -> c.accept(event));
    log.info("Alert recommendation completed.");
  }

  private class BridgeRecommendationCommand implements Consumer<RecommendationCompletedEvent> {
    @Override
    public void accept(RecommendationCompletedEvent event) {
      if (!BridgeRecommendationCompletedEvent.class
          .isAssignableFrom(event.getClass())) {
        return;
      }

      BridgeRecommendationCompletedEvent bridgeEvent = (BridgeRecommendationCompletedEvent) event;

      var reason = RecommendationReason.valueOf(bridgeEvent.getReason());
      var alertId = bridgeEvent.getAlertId();
      var recommendationId = createRecommendationUseCase
          .createBridgeRecommendation(bridgeEvent.getAlertId(), reason);

      var status = map(reason);
      if (alertMessageUseCase.isOutdated(event)) {
        alertMessageStatusUseCase.transitionAlertMessageStatus(alertId, status, UNDELIVERED);
      } else {
        var transited = alertMessageStatusUseCase
            .transitionAlertMessageStatus(alertId, status, PENDING);
        if (transited) {
          applicationEventPublisher.publishEvent(
              buildResponseCompleted(alertId, recommendationId.getId(), status));
        }
      }
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
  }

  private ResponseCompleted buildResponseCompleted(UUID alertId, UUID recommendationId,
      AlertMessageStatus status) {
    return ResponseCompleted.newBuilder()
        .setAlert("alerts/" + alertId)
        .setStatus("alerts/" + alertId + "/status/" + status.name())
        .setRecommendation("alerts/" + alertId + "/recommendations/" + recommendationId)
        .build();
  }

  private class AdjudicationRecommendationCommand
      implements Consumer<RecommendationCompletedEvent> {

    @Override
    public void accept(
        RecommendationCompletedEvent event) {
      if (!AdjudicationRecommendationCompletedEvent.class
          .isAssignableFrom(event.getClass())) {
        return;
      }

      AdjudicationRecommendationCompletedEvent adjudicationEvent =
          (AdjudicationRecommendationCompletedEvent) event;

      MDC.put("recommendation",
          adjudicationEvent.getRecommendation().getRecommendation().getRecommendedAction());

      var alertId = adjudicationEvent.getAlertId();
      var recommendationId = createRecommendationUseCase
          .createAdjudicationRecommendation(alertId,adjudicationEvent.getRecommendation());

      if (!alertMessageUseCase.isResolvedOrOutdated(event)) {
        var transited = alertMessageStatusUseCase
            .transitionAlertMessageStatus(alertId, RECOMMENDED, PENDING);
        if (transited) {
          applicationEventPublisher.publishEvent(
              buildResponseCompleted(alertId, recommendationId.getId(), RECOMMENDED));
        }
      }
    }
  }
}
