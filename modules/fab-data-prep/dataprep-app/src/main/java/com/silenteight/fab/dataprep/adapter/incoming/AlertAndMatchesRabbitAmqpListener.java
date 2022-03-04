package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.fab.dataprep.domain.FeedingFacade;
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

  @RabbitListener(queues = QUEUE_NAME_PROPERTY)
  public void subscribe(MessageAlertAndMatchesStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}, alert name: {}", message.getBatchId(),
        message.getAlertId(), message.getAlertName());

    feedingFacade.feedUds(message);
  }
}
