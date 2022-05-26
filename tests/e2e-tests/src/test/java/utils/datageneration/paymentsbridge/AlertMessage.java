/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Getter
@Builder
public class AlertMessage {

  @JsonProperty("SystemID")
  private String alertId;

  @JsonProperty("MessageID")
  private String messageId;

  @JsonProperty("MessageData")
  private String messageData;

  @JsonProperty("Hits")
  private List<AlertHit> hits;
}
