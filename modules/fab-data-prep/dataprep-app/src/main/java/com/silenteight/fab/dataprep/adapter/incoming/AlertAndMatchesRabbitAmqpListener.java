package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.AlertParser;
import com.silenteight.fab.dataprep.domain.FeedingFacade;
import com.silenteight.fab.dataprep.domain.MessageDataTokenizer;
import com.silenteight.fab.dataprep.domain.RegistrationService;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;
import com.silenteight.proto.fab.api.v1.MessageAlertStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertAndMatchesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-and-matches-stored.queue-name}";

  private final FeedingFacade feedingFacade;

  private final AlertDetailsFacade alertDetailsFacade;

  private final MessageDataTokenizer messageDataTokenizer;

  private final AlertParser alertParser;

  private final RegistrationService registrationService;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(MessageAlertStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}", message.getBatchId(),
        message.getAlertId());
    AlertsDetailsResponse alertsDetailsResponse = getAlertDetails(message);

    List<ExtractedAlert> extractedAlerts = getExtractedAlerts(message, alertsDetailsResponse);
    registrationService.registerAlertsAndMatches(extractedAlerts);
    extractedAlerts.forEach(feedingFacade::etlAndFeedUds);
  }

  private List<ExtractedAlert> getExtractedAlerts(MessageAlertStored message,
      AlertsDetailsResponse alertsDetailsResponse) {
    return alertsDetailsResponse.getAlertsList()
        .stream()
        .map(alertDetails -> alertParser.parse(message, alertDetails))
        .collect(toList());
  }

  private AlertsDetailsResponse getAlertDetails(MessageAlertStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

}
