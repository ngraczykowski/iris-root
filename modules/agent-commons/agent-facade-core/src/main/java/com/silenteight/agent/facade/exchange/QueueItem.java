package com.silenteight.agent.facade.exchange;

import lombok.Data;

@Data
public class QueueItem {

  private String inboundExchangeName;
  private String inboundQueueName;
  private String inboundRoutingKey;

  private String outboundExchangeName;

  private String deadLetterExchangeName;
  private String deadLetterQueueName;
  private String deadLetterRoutingKey;
}
