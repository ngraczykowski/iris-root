package com.silenteight.fab.dataprep.adapter.incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.fab.api.v1.MessageAlertAndMatchesStored;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AlertAndMatchesRabbitAmqpListener {

  @RabbitListener(queues = "${amqp.dataprep.incoming.alert-and-matches-stored.queue-name}")
  public void subscribe(MessageAlertAndMatchesStored message) {
    log.info(
        "Received a message with: batch id: {}, alert id: {}, alert name: {}", message.getBatchId(),
        message.getAlertId(), message.getAlertName());
  }
}
