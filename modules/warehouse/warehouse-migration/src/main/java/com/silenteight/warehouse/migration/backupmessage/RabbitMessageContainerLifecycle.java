package com.silenteight.warehouse.migration.backupmessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
class RabbitMessageContainerLifecycle {

  private final AtomicBoolean pausedDuringInitialization = new AtomicBoolean(false);

  private final List<IntegrationFlow> queueBeans;

  public void startRabbitListeners() {
    queueBeans.forEach(this::startRabbitMqListener);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void stopRabbitListeners() {
    if (pausedDuringInitialization.compareAndSet(false, true)) {
      queueBeans.forEach(this::stopRabbitMqListener);
    }
  }

  private void startRabbitMqListener(IntegrationFlow flow) {
    getMessageListenerContainer(flow, Predicate.not(AbstractMessageListenerContainer::isRunning))
        .ifPresent(container -> {
          container.start();
          log.info("Starting rabbitmq container listener: {}", container.getListenerId());
        });
  }

  private void stopRabbitMqListener(IntegrationFlow flow) {
    getMessageListenerContainer(flow, AbstractMessageListenerContainer::isRunning)
        .ifPresent(container -> {
          container.stop();
          log.info("Stopping rabbitmq container listener: {}", container.getListenerId());
        });
  }

  private Optional<SimpleMessageListenerContainer> getMessageListenerContainer(
      IntegrationFlow integrationFlow,
      Predicate<AbstractMessageListenerContainer> statePredicate) {

    if (integrationFlow instanceof StandardIntegrationFlow) {
      return ((StandardIntegrationFlow) integrationFlow)
          .getIntegrationComponents().keySet()
          .stream()
          .filter(SimpleMessageListenerContainer.class::isInstance)
          .map(SimpleMessageListenerContainer.class::cast)
          .filter(statePredicate)
          .findFirst();
    }
    return Optional.empty();
  }
}
