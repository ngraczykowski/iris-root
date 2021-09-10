package com.silenteight.payments.bridge.app.amqp;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.silenteight.payments.bridge.app.amqp.AmqpDefaults.*;
import static org.springframework.amqp.core.ExchangeBuilder.topicExchange;

@Configuration
@Profile("rabbitdeclare")
class RabbitBrokerConfiguration {

  private static final String ERROR_QUEUE = "pb.error-queue";
  private static final String DEAD_LETTER_QUEUE = "pb.dlq";
  private static final String DEAD_LETTER_EXCHANGE = "pb.dlx";

  @Bean
  Declarables rabbitBrokerDeclarables() {
    var errorQueue = queue(ERROR_QUEUE).build();
    var deadLetterQueue = queue(DEAD_LETTER_QUEUE).lazy().build();

    var commandExchange = topicExchange(COMMAND_EXCHANGE_NAME).durable(true).build();
    var eventExchange = topicExchange(EVENT_EXCHANGE_NAME).durable(true).build();
    var eventInternalExchange = topicExchange(EVENT_INTERNAL_EXCHANGE_NAME)
        .durable(true)
        .build();
    var deadLetterExchange = topicExchange(DEAD_LETTER_EXCHANGE)
        .durable(true)
        .internal()
        .build();
    var deadLetterBinding = bind(deadLetterQueue, deadLetterExchange, "#");

    var fircoCommand = queueDeadLetter(FIRCO_COMMAND_QUEUE_NAME).maxPriority(10).build();
    var fircoCommandBinding = bind(fircoCommand, commandExchange, FIRCO_COMMAND_PREFIX + ".#");

    return new Declarables(
        errorQueue,
        deadLetterExchange,
        deadLetterQueue, deadLetterBinding,
        commandExchange, eventExchange, eventInternalExchange,
        fircoCommand,
        fircoCommandBinding);
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
