package com.silenteight.adjudication.engine.solving.application.listener;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.BojkaBajkaIBraworka;
import com.silenteight.adjudication.internal.v1.AnalysisAlertsAdded;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class ReceivedAlertListener {

  private final BojkaBajkaIBraworka bojkaBajkaIBraworka;

  ReceivedAlertListener(BojkaBajkaIBraworka bojkaBajkaIBraworka) {
    this.bojkaBajkaIBraworka = bojkaBajkaIBraworka;
  }

  @RabbitListener(
      autoStartup = "true",
      bindings = @QueueBinding(value = @Queue(name = "ae.solving-received-alert"),
      exchange = @Exchange(name = "ae.event.internal", type = ExchangeTypes.TOPIC),
      key = "ae.event.analysis-alerts-added"))
  @Timed(percentiles = { 0.5, 0.95, 0.99 }, histogram = true)
  void onReceivedAlert(AnalysisAlertsAdded message) {
    log.info("Received alert message with alerts count {}", message.getAnalysisAlertsCount());

    // Get alerts matches and features
    // Transform to InMemoryObject and store in memory (TTL)
    // Send Tracking Event
    // Send Matches to Agent (event tracking on each send)

    this.bojkaBajkaIBraworka.handle(message);

  }

}
