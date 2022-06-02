package com.silenteight.agent.facade.exchange;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.integration.amqp.dsl.SimpleMessageListenerContainerSpec;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@NoArgsConstructor(access = PRIVATE)
class DeleteQueueWithoutPrioritySupportUseCase {

  static void deleteIfEmptyQueueWithoutPrioritySupport(
      AmqpAdmin amqpAdmin, String queueName, SimpleMessageListenerContainerSpec container) {
    var queueInformation = amqpAdmin.getQueueInfo(queueName);

    if (queueInformation == null) {
      log.debug("Queue {} without priority support doesnt exist", queueName);
    } else if (queueInformation.getMessageCount() == 0) {
      log.debug("Removing queue {} without priority support", queueName);
      amqpAdmin.deleteQueue(queueName);
    } else {
      log.debug(
          "Queue {} without priority support contains messages and needs to be added", queueName);
      container.addQueueNames(queueName);
    }
  }
}
