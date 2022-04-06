package com.silenteight.connector.ftcc.common.dto.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class RequestDto implements Serializable {

  private static final long serialVersionUID = -2318468779346806189L;

  private transient String header;

  @NotNull
  @Valid
  private RequestBodyDto body;

  public long getMessagesCount() {
    return body.getMessagesCount();
  }

  public List<JsonNode> getMessages() {
    return body.getMessages();
  }
}
