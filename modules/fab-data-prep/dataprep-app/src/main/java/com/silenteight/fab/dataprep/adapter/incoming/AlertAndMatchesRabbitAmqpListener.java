package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.AlertParser;
import com.silenteight.fab.dataprep.domain.FeedingFacade;
import com.silenteight.fab.dataprep.domain.RegistrationService;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;
import com.silenteight.proto.fab.api.v1.AlertDetails;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;
import com.silenteight.proto.fab.api.v1.MessageAlertStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertAndMatchesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-and-matches-stored.queue-name}";

  private final FeedingFacade feedingFacade;

  private final AlertDetailsFacade alertDetailsFacade;

  private final AlertParser alertParser;

  private final RegistrationService registrationService;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(MessageAlertStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}", message.getBatchId(),
        message.getAlertId());
    AlertsDetailsResponse alertsDetailsResponse = getAlertDetails(message);
    Map<String, ExtractedAlert> extractedAlerts =
        getExtractedAlerts(message, alertsDetailsResponse);
    List<RegisteredAlert> registeredAlerts =
        registrationService.registerAlertsAndMatches(extractedAlerts);
    registeredAlerts.forEach(feedingFacade::etlAndFeedUds);
  }

  private Map<String, ExtractedAlert> getExtractedAlerts(
      MessageAlertStored message,
      AlertsDetailsResponse alertsDetailsResponse) {
    return alertsDetailsResponse.getAlertsList()
        .stream()
        //.map(alertDetails -> alertParser.parse(message, alertDetails))
        .collect(toMap(AlertDetails::getAlertId, ad -> alertParser.parse(message, ad)));
  }

  private AlertsDetailsResponse getAlertDetails(MessageAlertStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

}
