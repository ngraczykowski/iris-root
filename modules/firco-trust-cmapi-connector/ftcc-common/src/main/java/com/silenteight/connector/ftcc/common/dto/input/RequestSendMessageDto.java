package com.silenteight.connector.ftcc.common.dto.input;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestSendMessageDto {

  String versionTag;

  /**
   * List of parameters used to connect to Case Manager, it is Payments Bridge in our case.
   */
  @Valid
  CaseManagerAuthenticationDto authentication;

  @Size(min = 1)
  @NotNull
  @Valid
  List<JsonNode> messages;

  long getMessagesCount() {
    return messages.size();
  }
}
