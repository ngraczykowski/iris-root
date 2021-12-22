package com.silenteight.warehouse.migration.backupmessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
class RabbitMessageContainerLifecycle {

  private final AtomicBoolean pausedDuringInitialization = new AtomicBoolean(false);

  private final List<IntegrationFlow> queueBeans;

  public void startRabbitListeners() {
    log.info("Starting all rabbitmq container listeners");
    queueBeans.forEach(this::startRabbitMqListener);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void stopRabbitListeners() {
    if (pausedDuringInitialization.compareAndSet(false, true)) {
      log.info("Stopping all rabbitmq container listeners");
      queueBeans.forEach(this::stopRabbitMqListener);
    }
  }

  private void startRabbitMqListener(IntegrationFlow integrationFlow) {
    if (integrationFlow instanceof StandardIntegrationFlow) {
      ((StandardIntegrationFlow) integrationFlow).start();
    }
  }

  private void stopRabbitMqListener(IntegrationFlow integrationFlow) {
    if (integrationFlow instanceof StandardIntegrationFlow) {
      ((StandardIntegrationFlow) integrationFlow).stop();
    }
  }
}
