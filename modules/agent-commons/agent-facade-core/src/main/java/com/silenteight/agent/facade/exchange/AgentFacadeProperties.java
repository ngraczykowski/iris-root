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
  /**
   * @deprecated Can be removed when every agent request queue without priority support will be
   *     removed from rabbit. Timeline of queues removal depends on specified condition: - if queue
   *     contains no messages it will be removed on agent start with rabbitmq-declare profile - on
   *     the other hand if agent starts with same profile and queue contains messages it will be
   *     still consumed, but won't receive any new messages, during restart it will be completely
   *     removed
   */
  @Deprecated
  private String inboundQueueName;
  private String inboundQueueWithPrioritySupportName;
  private String inboundRoutingKey;
  private int maxQueuePriority;

  private String outboundExchangeName;

  private String deadLetterExchangeName;
  private String deadLetterQueueName;
  private String deadLetterRoutingKey;

  private Map<String, QueueItem> queueDefinitions;

  private int parallelism = 1;

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
