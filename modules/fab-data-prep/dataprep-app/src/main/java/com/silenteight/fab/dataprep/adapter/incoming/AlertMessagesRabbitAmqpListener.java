package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.AlertParser;
import com.silenteight.fab.dataprep.domain.FeedingFacade;
import com.silenteight.fab.dataprep.domain.RegistrationService;
import com.silenteight.fab.dataprep.domain.model.AlertErrorDescription;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.proto.fab.api.v1.AlertMessageDetails;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse;

import com.google.common.base.Stopwatch;
import io.vavr.control.Try;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.List.of;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertMessagesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-message.queue-name}";

  private final FeedingFacade feedingFacade;

  private final AlertDetailsFacade alertDetailsFacade;

  private final AlertParser alertParser;

  private final RegistrationService registrationService;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(AlertMessageStored message) {
    try {
      log.info(
          "Received a message with: batch: {}, alert message: {}", message.getBatchName(),
          message.getMessageName());
      Stopwatch stopwatch = Stopwatch.createStarted();

      Try.of(() -> registerAlert(message))
          .getOrElseGet(e -> registerFailedAlert(message))
          .forEach(feedingFacade::etlAndFeedUds);

      long alertMessageHandlingDuration = stopwatch.elapsed(TimeUnit.MILLISECONDS);
      log.info(
          "Batch: {}, alert message: {} handled in {} millis", message.getBatchName(),
          message.getMessageName(), alertMessageHandlingDuration);
    } catch (Exception e) {
      log.warn("Unable to handle message", e);
      throw new AmqpRejectAndDontRequeueException(e);
    }
  }

  private List<RegisteredAlert> registerAlert(AlertMessageStored message) {
    AlertMessagesDetailsResponse response = getAlertDetails(message);
    Map<String, ParsedAlertMessage> extractedAlerts = getExtractedAlerts(message, response);
    return registrationService.registerAlertsAndMatches(extractedAlerts);
  }

  private List<RegisteredAlert> registerFailedAlert(AlertMessageStored message) {
    return registrationService.registerFailedAlerts(of(message.getMessageName()),
        message.getBatchName(), AlertErrorDescription.EXTRACTION);
  }

  private Map<String, ParsedAlertMessage> getExtractedAlerts(
      AlertMessageStored message,
      AlertMessagesDetailsResponse alertsDetailsResponse) {
    return alertsDetailsResponse.getAlertsList()
        .stream()
        .collect(toMap(AlertMessageDetails::getMessageName, ad -> alertParser.parse(message, ad)));
  }

  private AlertMessagesDetailsResponse getAlertDetails(AlertMessageStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

}
