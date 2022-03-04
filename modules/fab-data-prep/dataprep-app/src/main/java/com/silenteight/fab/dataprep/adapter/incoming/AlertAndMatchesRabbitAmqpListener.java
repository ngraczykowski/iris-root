package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.FeedingFacade;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;
import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertAndMatchesRabbitAmqpListener {

  public static final String QUEUE_NAME_PROPERTY =
      "${amqp.dataprep.incoming.alert-and-matches-stored.queue-name}";

  private final FeedingFacade feedingFacade;

  private final AlertDetailsFacade alertDetailsFacade;

  //TODO: Why only one incoming alert? not many?
  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(MessageAlertAndMatchesStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}, alert name: {}", message.getBatchId(),
        message.getAlertId(), message.getAlertName());
    AlertsDetailsResponse alertsDetailsResponse = getAlertDetails(message);
    createFeatureInput(alertsDetailsResponse);
    feedUds();
  }

  private void feedUds() {
    feedingFacade.feedUds();
    sendMatchFeatureInputFeed();
  }

  private void sendMatchFeatureInputFeed() {
    log.info("Have to be implemented");
  }


  private void createFeatureInput(AlertsDetailsResponse alertsDetailsResponse) {
    log.info("Have to be implemented");
  }

  private AlertsDetailsResponse getAlertDetails(MessageAlertAndMatchesStored message) {
    return alertDetailsFacade.getAlertDetails(message);
  }

}
