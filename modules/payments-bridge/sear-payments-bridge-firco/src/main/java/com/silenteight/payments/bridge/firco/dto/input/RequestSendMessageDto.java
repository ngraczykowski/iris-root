package com.silenteight.payments.bridge.firco.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestSendMessageDto implements Serializable {

  private static final long serialVersionUID = -7868232254808751898L;

  @JsonProperty("VersionTag")
  private String versionTag;

  /**
   * List of parameters used to connect to Case Manager, it is Payments Bridge in our case.
   */
  @JsonProperty("Authentication")
  private CaseManagerAuthenticationDto authentication;

  @JsonProperty("Messages")
  @Size(min = 1)
  @NotNull
  @Valid
  private List<RequestMessageDto> messages;

  List<AlertMessageDto> getAlerts() {
    return messages
        .stream()
        .map(RequestMessageDto::getMessage)
        .collect(Collectors.toList());
  }
}
