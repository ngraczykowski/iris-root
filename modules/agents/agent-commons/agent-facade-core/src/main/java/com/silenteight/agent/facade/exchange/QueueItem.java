package com.silenteight.agent.facade.exchange;

import lombok.Data;

@Data
public class QueueItem {

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
}
