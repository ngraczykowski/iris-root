package com.silenteight.serp.governance.bulkchange.integration;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.silenteight.serp.governance.common.MessagingConstants.*;

@Data
@Validated
@ConfigurationProperties(prefix = "serp.governance.integration.bulk-change")
class BulkChangeAmqpIntegrationProperties {

  @Valid
  @NotNull
  BulkChangeCommandAmqpProperties create = BulkChangeCommandAmqpProperties.builder()
      .inboundQueueName(QUEUE_GOVERNANCE_BULK_CHANGE_CREATE)
      .outboundExchangeName(EXCHANGE_BULK_CHANGE)
      .outboundRoutingKey(ROUTE_BULK_CREATED)
      .build();

  @Valid
  @NotNull
  BulkChangeCommandAmqpProperties apply = BulkChangeCommandAmqpProperties.builder()
      .inboundQueueName(QUEUE_GOVERNANCE_BULK_CHANGE_APPLY)
      .outboundExchangeName(EXCHANGE_BULK_CHANGE)
      .outboundRoutingKey(ROUTE_BULK_APPLIED)
      .build();

  @Valid
  @NotNull
  BulkChangeCommandAmqpProperties reject = BulkChangeCommandAmqpProperties.builder()
      .inboundQueueName(QUEUE_GOVERNANCE_BULK_CHANGE_REJECT)
      .outboundExchangeName(EXCHANGE_BULK_CHANGE)
      .outboundRoutingKey(ROUTE_BULK_REJECTED)
      .build();
}
