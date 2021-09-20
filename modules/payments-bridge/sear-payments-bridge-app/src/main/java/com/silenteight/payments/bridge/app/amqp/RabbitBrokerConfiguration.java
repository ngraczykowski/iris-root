package com.silenteight.payments.bridge.app.amqp;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_ALERT_STORED_ROUTING_KEY;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_COMMAND_QUEUE_NAME;
import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.FIRCO_EXCHANGE_NAME;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;

@Configuration
class RabbitBrokerConfiguration {

  private static final String ERROR_QUEUE = "pb.error-queue";
  private static final String DEAD_LETTER_QUEUE = "pb.dlq";
  private static final String DEAD_LETTER_EXCHANGE = "pb.dlx";

  @Bean
  Declarables rabbitBrokerDeclarables() {

    var exchange = topicExchange(FIRCO_EXCHANGE_NAME).durable(true).build();
    var commandQueue = queue(FIRCO_COMMAND_QUEUE_NAME)
        .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
        .maxPriority(10).build();
    var commandQueueBinding = bind(commandQueue, exchange, FIRCO_ALERT_STORED_ROUTING_KEY + ".#");

    var errorQueue = queue(ERROR_QUEUE).build();
    var deadLetterQueue = queue(DEAD_LETTER_QUEUE).lazy().build();
    var deadLetterExchange = topicExchange(DEAD_LETTER_EXCHANGE)
        .durable(true)
        .internal()
        .build();
    var deadLetterBinding = bind(deadLetterQueue, deadLetterExchange, "#");

    return new Declarables(
        exchange,
        commandQueue,
        commandQueueBinding,
        errorQueue,
        deadLetterExchange,
        deadLetterQueue, deadLetterBinding);
  }

  private static QueueBuilder queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .withArgument("x-queue-type", "classic");
  }

  private static QueueBuilder queueDeadLetter(String queueName) {
    return queue(queueName).deadLetterExchange(DEAD_LETTER_EXCHANGE);
  }

  private static Binding bind(Queue queue, Exchange exchange, String routingKey) {
    return BindingBuilder
        .bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
