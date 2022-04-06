package com.silenteight.connector.ftcc.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestBodyDto implements Serializable {

  private static final long serialVersionUID = -1093820420334687526L;

  @JsonProperty("msg_SendMessage")
  @NotNull
  @Valid
  private RequestSendMessageDto sendMessageDto;

  long getMessagesCount() {
    return sendMessageDto.getMessagesCount();
  }

  List<JsonNode> getMessages() {
    return sendMessageDto.getMessages();
  }
}
