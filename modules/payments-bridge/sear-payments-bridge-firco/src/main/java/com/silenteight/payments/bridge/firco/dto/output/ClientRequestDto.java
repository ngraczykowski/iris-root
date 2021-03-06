package com.silenteight.payments.bridge.firco.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@JsonNaming(UpperCamelCaseStrategy.class)
public class ClientRequestDto implements Serializable {

  private static final long serialVersionUID = 4361457566013326051L;

  private String header;

  private Body body;

  public static class Body {
    @JsonProperty("msg_ReceiveDecision")
    private ReceiveDecisionDto receiveDecisionDto;
  }

  public void setReceiveDecisionDto(ReceiveDecisionDto receiveDecisionDto) {
    if (body == null) {
      body = new Body();
    }
    body.receiveDecisionDto = receiveDecisionDto;
  }

  public List<String> getSystemIds() {
    return body.receiveDecisionDto.getMessages().stream()
        .map(a -> a.getDecisionMessage().getSystemID())
        .collect(toList());
  }
}
