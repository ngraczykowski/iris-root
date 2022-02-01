package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.alertmessage.port.AlertMessagePayloadUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.firco.datasource.port.CmapiNotificationCreatorUseCase;
import com.silenteight.payments.bridge.firco.datasource.port.CreateDatasourceInputsUseCase;
import com.silenteight.payments.bridge.firco.datasource.port.EtlUseCase;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.RecommendationReason;
import com.silenteight.payments.bridge.firco.recommendation.port.CreateRecommendationUseCase;
import com.silenteight.payments.bridge.notification.model.NotificationEvent;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class EtlService implements EtlUseCase {

  private final ExtractAlertEtlResponseUseCase extractAlertEtlResponseUseCase;
  private final AlertMessagePayloadUseCase alertMessagePayloadUseCase;
  private final CreateRecommendationUseCase createRecommendationUseCase;
  private final CmapiNotificationCreatorUseCase cmapiNotificationCreatorUseCase;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final CreateDatasourceInputsUseCase createDatasourceInputs;

  @LogContext
  @Override
  public void process(AeAlert alert) {
    var alertId = alert.getAlertId();
    var alertName = alert.getAlertName();
    MDC.put("alertId", alertId.toString());
    MDC.put("alertName", alertName);

    var alertMessageDto = getAlertMessageDto(alert);

    try {
      var alertEtlResponse = extractAlertEtlResponseUseCase.createAlertEtlResponse(alertMessageDto);
      createDatasourceInputs.processStructured(alert, alertEtlResponse.getHits());

      var hitAndWatchlistPartyData =
          extractAlertEtlResponseUseCase.getWatchlistDataForMatch(alertMessageDto);
      createDatasourceInputs.processUnstructured(alert, hitAndWatchlistPartyData);

    } catch (UnsupportedMessageException exception) {
      log.error("Failed to process a message payload associated with the alert: {}. "
          + "Reject the message as DAMAGED", alertId, exception);
      var cmapiNotificationRequest = CmapiNotificationRequest
          .builder()
          .alertId(alertId.toString())
          .alertName(alertName)
          .messageId(alertMessageDto.getMessageID())
          .systemId(alertMessageDto.getSystemID())
          .message(exception.getMessage())
          .build();

      var notificationEvent = new NotificationEvent(
          cmapiNotificationCreatorUseCase.createCmapiNotification(cmapiNotificationRequest));

      applicationEventPublisher.publishEvent(notificationEvent);

      createRecommendationUseCase.create(
          new BridgeSourcedRecommendation(alertId, AlertMessageStatus.REJECTED_DAMAGED.name(),
              RecommendationReason.DAMAGED.name()));
      throw exception;
    }
  }

  private AlertMessageDto getAlertMessageDto(AeAlert alert) {
    var alertId = alert.getAlertId();
    var alertMessageDto = alertMessagePayloadUseCase.findByAlertMessageId(alertId);

    MDC.put("systemId", alertMessageDto.getSystemID());
    return alertMessageDto;
  }
}
