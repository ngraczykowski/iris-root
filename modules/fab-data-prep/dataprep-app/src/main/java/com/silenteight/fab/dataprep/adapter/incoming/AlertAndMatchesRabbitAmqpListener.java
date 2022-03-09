package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.FeedingFacade;
import com.silenteight.fab.dataprep.domain.TransformService;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.proto.fab.api.v1.AlertDetails;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;

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

  private final TransformService transformService;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(MessageAlertAndMatchesStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}", message.getBatchId(),
        message.getAlertId());
    AlertsDetailsResponse alertsDetailsResponse = getAlertDetails(message);

    List<ExtractedAlert> extractedAlerts = getExtractedAlerts(message, alertsDetailsResponse);
    extractedAlerts.forEach(feedingFacade::etlAndFeedUds);
  }

  private List<ExtractedAlert> getExtractedAlerts(
      MessageAlertAndMatchesStored message,
      AlertsDetailsResponse alertsDetailsResponse) {
    return alertsDetailsResponse.getAlertsList()
        .stream()
        .map(alertDetails -> getExtractedAlert(message, alertDetails))
        .collect(toList());
  }

  private ExtractedAlert getExtractedAlert(
      MessageAlertAndMatchesStored message,
      AlertDetails alertDetails) {
    return ExtractedAlert.builder()
        .batchId(message.getBatchId())
        .alertId(message.getAlertId())
        .parsedPayload(transformService.convert(alertDetails.getPayload()))
        .build();
    //TODO set fields: alertName, status, errorDescription, matches
  }

  private AlertsDetailsResponse getAlertDetails(MessageAlertAndMatchesStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

}
