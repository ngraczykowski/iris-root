package com.silenteight.payments.bridge.app.integration.retention;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.dataretention.api.v1.AlertsExpired;
import com.silenteight.payments.bridge.ae.alertregistration.port.DeleteRegisteredAlertUseCase;
import com.silenteight.payments.bridge.firco.alertmessage.model.AlertIdSet;
import com.silenteight.payments.bridge.firco.alertmessage.port.FindAlertIdSetAccessPort;
import com.silenteight.payments.bridge.firco.retention.port.incoming.AlertRetentionUseCase;
import com.silenteight.sep.base.common.messaging.AmqpInboundFactory;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.AmqpInboundChannelAdapterSMLCSpec;
import org.springframework.integration.dsl.IntegrationFlow;

import javax.validation.Valid;

import static java.util.stream.Collectors.toList;
import static org.springframework.integration.dsl.IntegrationFlows.from;

@Configuration
@EnableConfigurationProperties(DataRetentionInboundAmqpIntegrationProperties.class)
@RequiredArgsConstructor
@Slf4j
class DataRetentionInboundAmqpIntegrationConfiguration {

  @Valid
  private final DataRetentionInboundAmqpIntegrationProperties properties;
  private final AmqpInboundFactory inboundFactory;
  private final AlertRetentionUseCase alertRetentionUseCase;
  private final DeleteRegisteredAlertUseCase deleteRegisteredAlertUseCase;
  private final FindAlertIdSetAccessPort findAlertIdSetAccessPort;

  @Bean
  IntegrationFlow alertDataRetentionInbound() {
    return from(createInboundAdapter(properties.getInboundQueueNames()))
        .handle(AlertsExpired.class, (payload, headers) -> {
          removeExpiredAlerts(payload);
          return payload;
        })
        .get();
  }

  private AmqpInboundChannelAdapterSMLCSpec createInboundAdapter(String... queueNames) {
    return inboundFactory
        .simpleAdapter()
        .configureContainer(c -> c.addQueueNames(queueNames));
  }

  private void removeExpiredAlerts(AlertsExpired alertsExpired) {
    var systemIds = deleteRegisteredAlertUseCase.delete(alertsExpired.getAlertsList());
    var alertIdSet = findAlertIdSetAccessPort.find(systemIds);
    var alertMessageIds = alertIdSet.stream().map(AlertIdSet::getAlertId).collect(toList());

    alertRetentionUseCase.invoke(alertMessageIds);
  }
}
