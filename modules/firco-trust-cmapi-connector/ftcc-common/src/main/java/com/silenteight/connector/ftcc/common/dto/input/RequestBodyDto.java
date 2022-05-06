package com.silenteight.connector.ftcc.common.dto.input;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestBodyDto {

  @JsonProperty("msg_SendMessage")
  @NotNull
  @Valid
  RequestSendMessageDto sendMessageDto;

  long getMessagesCount() {
    return sendMessageDto.getMessagesCount();
  }

  List<JsonNode> getMessages() {
    return sendMessageDto.getMessages();
  }
}
