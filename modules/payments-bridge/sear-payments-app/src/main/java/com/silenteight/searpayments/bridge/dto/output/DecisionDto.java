package com.silenteight.searpayments.bridge.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DecisionDto implements Serializable {

  private static final long serialVersionUID = 4361457566013326051L;

  @JsonProperty("msg_ReceiveDecision")
  ReceiveDecisionDto receiveDecisionDto;

  public String getOutputStatus() {
    int decisionsCount = getReceiveDecisionDto().getMessages().size();
    if (decisionsCount != 1) {
      log.error("There are {} decisions for the alert but should be 1", decisionsCount);
      throw new IllegalStateException();
    }
    MessageDto messageDto =
        getReceiveDecisionDto().getMessages().get(0).getMessageDto();
    return messageDto.getStatusDto().getName();
  }
}
