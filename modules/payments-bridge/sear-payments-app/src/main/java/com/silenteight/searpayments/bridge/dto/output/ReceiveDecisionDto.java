package com.silenteight.searpayments.bridge.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveDecisionDto implements Serializable {

  private static final long serialVersionUID = -4284460925751955090L;
  @JsonProperty("VersionTag")
  String versionTag;

  @JsonProperty("Authentication")
  AuthenticationDto authenticationDto;

  @JsonProperty("Messages")
  List<MessageToSendDto> messages;
}
