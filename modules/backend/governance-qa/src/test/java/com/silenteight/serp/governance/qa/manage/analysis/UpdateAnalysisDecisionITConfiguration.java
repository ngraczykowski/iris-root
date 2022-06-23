package com.silenteight.serp.governance.qa.manage.analysis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.database.HibernateCacheAutoConfiguration;
import com.silenteight.sep.base.common.messaging.IntegrationConfiguration;
import com.silenteight.sep.base.common.messaging.MessagingConfiguration;
import com.silenteight.sep.base.common.support.hibernate.SilentEightNamingConventionConfiguration;
import com.silenteight.serp.governance.common.grpc.GrpcCommonModule;
import com.silenteight.serp.governance.common.integration.AmqpCommonModule;
import com.silenteight.serp.governance.common.web.WebModule;
import com.silenteight.serp.governance.qa.QaIntegrationProperties;
import com.silenteight.serp.governance.qa.QaModule;

import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

import javax.validation.Valid;

import static java.util.Collections.emptyMap;
import static org.springframework.amqp.core.Binding.DestinationType.QUEUE;

@ComponentScan(basePackageClasses = {
    AmqpCommonModule.class,
    QaModule.class,
    GrpcCommonModule.class,
    WebModule.class,
})
@ImportAutoConfiguration({
    GrpcServerAutoConfiguration.class,
    GrpcServerFactoryAutoConfiguration.class,
    GrpcClientAutoConfiguration.class,
    HibernateCacheAutoConfiguration.class,
    IntegrationConfiguration.class,
    MessagingConfiguration.class,
    RabbitAutoConfiguration.class,
    SilentEightNamingConventionConfiguration.class
})
@EnableIntegration
@EnableIntegrationManagement
@RequiredArgsConstructor
@EnableConfigurationProperties(QaIntegrationProperties.class)
@Slf4j
class UpdateAnalysisDecisionITConfiguration {

  @MockBean
  AuditingLogger auditingLogger;

  @Bean
  Binding govEventsToQueueBinding(@Valid QaIntegrationProperties properties) {
    return bindQueue(
        properties.getReceive().getQueueName(),
        properties.getRequest().getExchange(),
        properties.getRequest().getRoutingKey());
  }

  @Bean
  Declarables govEventsDeclarables(@Valid QaIntegrationProperties properties) {
    return new Declarables(
        topicExchange(properties.getRequest().getExchange()),
        queue(properties.getReceive().getQueueName())
    );
  }

  private static TopicExchange topicExchange(String exchangeName) {
    return ExchangeBuilder
        .topicExchange(exchangeName)
        .durable(true)
        .build();
  }

  private static Queue queue(String queueName) {
    return QueueBuilder
        .durable(queueName)
        .build();
  }

  private static Binding bindQueue(String queueName, String exchangeName, String routingKey) {
    return new Binding(queueName, QUEUE, exchangeName, routingKey, emptyMap());
  }
}
