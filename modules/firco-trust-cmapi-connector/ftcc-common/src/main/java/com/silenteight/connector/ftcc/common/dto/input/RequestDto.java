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

@Value
@Builder
@Jacksonized
@JsonNaming(UpperCamelCaseStrategy.class)
public class RequestDto {

  transient String header;

  @NotNull
  @Valid
  RequestBodyDto body;

  public long getMessagesCount() {
    return body.getMessagesCount();
  }

  public List<JsonNode> getMessages() {
    return body.getMessages();
  }
}
