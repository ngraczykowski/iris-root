package com.silenteight.agent.facade.exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@ConfigurationProperties("facade.amqp")
@NoArgsConstructor
@Data
public class AgentFacadeProperties {

  private static final String DEFAULT_AGENT_REQUEST_EXCHANGE = "agent.request";
  private static final String DEFAULT_AGENT_RESPONSE_EXCHANGE = "agent.response";

  private String inboundExchangeName;
  private String inboundQueueName;
  private String inboundRoutingKey;

  private String outboundExchangeName;

  private String deadLetterExchangeName;
  private String deadLetterQueueName;
  private String deadLetterRoutingKey;

  private Map<String, QueueItem> queueDefinitions;

  String getInboundExchangeName() {
    return isNotEmpty(inboundExchangeName) ?
           inboundExchangeName : DEFAULT_AGENT_REQUEST_EXCHANGE;
  }

  String getOutboundExchangeName() {
    return isNotEmpty(outboundExchangeName) ?
           outboundExchangeName : DEFAULT_AGENT_RESPONSE_EXCHANGE;
  }

  QueueItem getQueueDefinitionByFacadeName(String facadeName) {
    return queueDefinitions.get(facadeName);
  }
}
