package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService;
import com.silenteight.scb.ingest.domain.port.outgoing.IngestEventPublisher;
import com.silenteight.sep.base.common.messaging.MessageSenderFactory;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;

import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collection;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Import(MessagingConfiguration.class)
class IngestConfiguration {

  private final IngestProperties properties;
  private final IngestEventPublisher ingestEventPublisher;

  @Bean
  Declarables rabbitExchange() {
    return new Declarables(
        ExchangeBuilder
            .fanoutExchange(properties.getOutputExchange())
            .durable(true)
            .build(),
        ExchangeBuilder
            .topicExchange(properties.getReportOutputExchange())
            .durable(true)
            .build());
  }

  @Bean
  IngestService ingestService(
      MessageSenderFactory senderFactory,
      Collection<IngestServiceListener> listeners,
      ScbRecommendationService scbRecommendationService) {

    return IngestService.builder()
        .sender(senderFactory.get(properties.getOutputExchange()))
        .listeners(listeners)
        .solvedAlertsProcessingEnabled(properties.isSolvedAlertsProcessingEnabled())
        .scbRecommendationService(scbRecommendationService)
        .ingestEventPublisher(ingestEventPublisher)
        .build();
  }

  @Bean
  @ConditionalOnBean(IngestService.class)
  IngestServiceMetrics ingestServiceMetrics(IngestService ingestService) {
    return new IngestServiceMetrics(ingestService);
  }
}
