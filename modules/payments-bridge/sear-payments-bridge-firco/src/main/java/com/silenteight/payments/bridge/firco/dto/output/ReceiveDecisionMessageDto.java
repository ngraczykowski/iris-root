package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveDecisionMessageDto implements Serializable {

  private static final long serialVersionUID = -1310665198348343534L;

  @JsonProperty("Message")
  private AlertDecisionMessageDto decisionMessage;
}
