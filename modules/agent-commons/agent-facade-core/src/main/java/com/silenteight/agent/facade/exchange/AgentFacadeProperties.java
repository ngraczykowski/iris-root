package com.silenteight.agent.facade.exchange;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.AGENT_REQUEST_EXCHANGE;
import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.AGENT_RESPONSE_EXCHANGE;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@ConfigurationProperties("facade.amqp")
@NoArgsConstructor
@Data
public class AgentFacadeProperties {


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
           inboundExchangeName : AGENT_REQUEST_EXCHANGE;
  }

  String getOutboundExchangeName() {
    return isNotEmpty(outboundExchangeName) ?
           outboundExchangeName : AGENT_RESPONSE_EXCHANGE;
  }

  QueueItem getQueueDefinitionByFacadeName(String facadeName) {
    return queueDefinitions.get(facadeName);
  }
}
