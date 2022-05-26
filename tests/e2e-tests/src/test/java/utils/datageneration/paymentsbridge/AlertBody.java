/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.paymentsbridge;

import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Builder
public class AlertBody {

  @JsonProperty("msg_SendMessage")
  private AlertSendMessage sendMessage;
}
